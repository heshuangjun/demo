import cn.pyg.pojo.TbItem;
import cn.pyg.solr.util.SolrUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private SolrUtil solrUtil;

    /**
     * 948条商品数据的导入测试
     */
    @Test
    public void dataImportTest(){
        solrUtil.dataImport();
    }


    /**
     * 添加索引库,修改其实也是这个方法,只要id控制一样,达到修改的效果!
     */
//    @Test
//    public void addTest() {
//        TbItem tbItem = new TbItem();
//        tbItem.setId(1L);
//        tbItem.setBrand("苹果");
//        tbItem.setTitle("小米最新款手环 移动3G 16G");
//        tbItem.setSeller("苹果旗舰店");
//        solrTemplate.saveBean(tbItem);
//        solrTemplate.commit();
//    }

    /**
     * 查询索引库,基于id
     */
    @Test
    public void queryByIdTest() {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item.getBrand());
        System.out.println(item.getTitle());
        System.out.println(item.getSeller());
    }

    /**
     * 删除索引库,基于id
     */
    @Test
    public void delByIdTest() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    /**
     * 删除索引库,基于查询删除
     */
    @Test
    public void delByExampleTest() {
        //*:* 查询所有的语法
        SolrDataQuery query = new SimpleQuery("*:*");
        //SolrDataQuery query = new SimpleQuery("id:1");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    /**
     * 批量的添加
     */
    @Test
    public void addMoreTest() {
        List<TbItem> itemsList = new ArrayList<>();

        for (long i = 1; i <= 100; i++) {
            TbItem tbItem = new TbItem();
            tbItem.setId(i);
            tbItem.setBrand("苹果");
            tbItem.setTitle(i + "小米最新款手环 移动3G 16G");
            tbItem.setSeller("苹果第" + i + "家旗舰店");
            itemsList.add(tbItem);
        }

        solrTemplate.saveBeans(itemsList);
        solrTemplate.commit();
    }

    /**
     * 分页查询
     */
    @Test
    public void pageQueryTest() {

        //参数一:查询所有
        Query query = new SimpleQuery("*:*");
        //设置分页的起始值
        query.setOffset(2);
        //设置每页查询记录的数
        query.setRows(5);
        //返回的是一个Page对象
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        System.out.println("获取满足条件查询的总记录数:" + page.getTotalElements());
        System.out.println("获取满足条件查询的总记录页面数:" + page.getTotalPages());
        //获取当前当页的记录的结果集
        List<TbItem> content = page.getContent();
        for (TbItem item : content) {
            System.out.println("ID:" + item.getId() + "   " + item.getBrand() + item.getTitle() + item.getSeller());
        }
    }

    /**
     * 条件查询&高亮显示
     */
    @Test
    public void moreElementsQuery() {

        //高亮查询对象
        HighlightQuery query = new SimpleHighlightQuery();

        //构建查询条件对象 (例如查询: item_title中包含数字5,再查询同时item_seller中包含9的商家)
        //Criteria criteria = new Criteria("item_title").contains("5").and("item_seller").contains("9");
        Criteria criteria = new Criteria("item_seller").contains("苹果");

        //将条件对象添加到查询对象里边
        query.addCriteria(criteria);

        //高亮设置
        HighlightOptions highlightOptions = new HighlightOptions();
        //需要制定高亮字段,及前缀后缀
        highlightOptions.addField("item_seller");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</front>");


        query.setHighlightOptions(highlightOptions);

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        System.out.println("获取满足条件查询的总记录数:" + page.getTotalElements());
        System.out.println("获取满足条件查询的总记录页面数:" + page.getTotalPages());
        //获取当前当页的记录的结果集
        List<TbItem> content = page.getContent();
        for (TbItem item : content) {

            //原有的结果是没有高亮显示的内容的,也就是没有<font>标签内容的,因为它被另外一个对象封装起来了
            //需要进行高亮结果的替换
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);
            if (highlights.size() > 0) {
                //说明有高亮的内容
                List<String> snipplets = highlights.get(0).getSnipplets();
                if (snipplets.size() > 0) {
                    //重新设置高亮字段,进行高亮替换
                    item.setSeller(snipplets.get(0));
                }
            }

            System.out.println("ID:" + item.getId() + "   " + item.getBrand() + item.getTitle() + item.getSeller());
        }
    }


}
