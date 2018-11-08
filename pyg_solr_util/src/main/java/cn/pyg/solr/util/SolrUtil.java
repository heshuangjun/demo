package cn.pyg.solr.util;

import cn.pyg.mapper.TbItemMapper;
import cn.pyg.pojo.TbItem;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper itemMapper;

    public void dataImport() {

        //查询所有上架的商品
        List<TbItem> itemList = itemMapper.findAllGrounding();

        //处理动态域 {"机身内存":"16G","网络":"移动4G"}
        for (TbItem item : itemList) {
            String spec = item.getSpec();
            Map specMap = JSON.parseObject(spec, Map.class);
            item.setSpecMap(specMap);
        }

        //查询出来的数据保存
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("数据导入成功!");
    }


}
