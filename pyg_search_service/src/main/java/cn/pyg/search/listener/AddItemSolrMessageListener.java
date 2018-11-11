package cn.pyg.search.listener;

import cn.pyg.mapper.TbItemMapper;
import cn.pyg.pojo.TbItem;
import cn.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @author heshuangjun
 * @date 2018-11-11 18:12
 */
public class AddItemSolrMessageListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        try {
            //同步上架的商品数据到索引库
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();

            //根据id查询需要同步的商品数据
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));//将String类型转化成Long类型;
            List<TbItem> itemList = itemMapper.selectByExample(example);

            //保存数据
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
