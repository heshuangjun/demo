package cn.pyg.search.service.impl;

import cn.pyg.pojo.TbItem;
import cn.pyg.search.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heshuangjun
 * @date 2018-11-06 18:26
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {


    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {


        //高亮查询对象
        HighlightQuery query = new SimpleHighlightQuery();


        /**
         * 1.关键字搜索 searchMap={keywords:"华为"}
         */
        String keywords = (String) searchMap.get("keywords");
        //构建条件查询对象
        Criteria criteria = null;
        if (keywords != null && !"".equals(keywords)) {
            //输入了关键字
            criteria = new Criteria("item_keywords").is(keywords);
        } else {
            //未输入关键字,查询所有
            criteria = new Criteria().expression("*:*");
        }

        /**
         * 品牌条件查询
         */
        String brand = (String) searchMap.get("brand");
        if (brand != null && !"".equals(brand)) {
            //构建品牌查询条件
            Criteria brandCriteria = new Criteria("item_brand").is(brand);
            //构建品牌过滤条件的对象
            FilterQuery filterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(filterQuery);//(总查询对象query)将过滤条件查询对象添加给高亮查询对象
        }

        /**
         * 3.分类条件查询
         */
        String category = (String) searchMap.get("category");
        if (category != null && !"".equals(category)) {
            //构建分类查询条件
            Criteria categoryCriteria = new Criteria("item_category").is(category);
            //构建分类过滤条件的对象
            FilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(filterQuery);//(总查询对象query)将过滤条件查询对象添加给高亮查询对象
        }

        /**
         * 4.价格过滤查询
         */
        String price = (String) searchMap.get("price");
        if (price != null && !"".equals(price)) {
            String[] prices = price.split("-");
            //0-500 500-1000 1000-* 条件价格封装,覆盖所有的价格区间.
            if (!"0".equals(prices[0])) {//价格条件大于起始值
                Criteria priceCriteria = new Criteria("item_price").greaterThan(prices[0]);
                //构建分类过滤条件的对象
                FilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);//(总查询对象query)将过滤条件查询对象添加给高亮查询对象
            }

            if (!"*".equals(prices[1])) {//价格条件小于等于最后值
                Criteria priceCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                //构建分类过滤条件的对象
                FilterQuery filterQuery = new SimpleFilterQuery(priceCriteria);
                query.addFilterQuery(filterQuery);//(总查询对象query)将过滤条件查询对象添加给高亮查询对象
            }
        }

        /**
         * 5.规格条件查询
         */
        Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
        for (String key : specMap.keySet()) {
            //构建规格的查询条件
            Criteria specCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
            //构建分类过滤条件的对象
            FilterQuery filterQuery = new SimpleFilterQuery(specCriteria);
            query.addFilterQuery(filterQuery);//(总查询对象query)将过滤条件查询对象添加给高亮查询对象
        }

        /**
         * 6.排序操作(价格和新品的排序)
         */
        String sortField = (String) searchMap.get("sortField");
        String sort = (String) searchMap.get("sort");
        if (sortField != null && !"".equals(sortField)) {
            //有排序条件
            if ("ASC".equals(sort)) {
                //升序
                query.addSort(new Sort(Sort.Direction.ASC, "item_" + sortField));
            } else {
                //降序
                query.addSort(new Sort(Sort.Direction.DESC, "item_" + sortField));
            }
        }


        /**
         * 7.分页查询
         */
            Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        query.setOffset((pageNo - 1) * pageSize);//设置起始值  0-59  60-119
        query.setRows(pageSize);//每页记录数

        //添加查询条件
        query.addCriteria(criteria);

        //高亮对象
        HighlightOptions highlightOptions = new HighlightOptions();
        //指定需要高亮的字段是什么,需要设置高亮内容的前缀和后缀
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<font color='blue'>");
        highlightOptions.setSimplePostfix("</font>");
        //高亮设置
        query.setHighlightOptions(highlightOptions);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);


        //把数据显示到页面的操作如下:(获取当前的结果集)
        List<TbItem> content = page.getContent();
        for (TbItem item : content) {
            //需要进行高亮结果替换操作
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            if (highlights.size() > 0) {
                //说明有高亮内容
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (snipplets.size() > 0) {
                    //重新设置高亮字段,进行高亮替换
                    item.setTitle(snipplets.get(0));
                }
            }
        }

        Map<String, Object> resultMap = new HashMap<>();


        resultMap.put("rows", content);
        resultMap.put("totalPages", page.getTotalPages());
        resultMap.put("pageNo", pageNo);


        return resultMap;
    }
}
