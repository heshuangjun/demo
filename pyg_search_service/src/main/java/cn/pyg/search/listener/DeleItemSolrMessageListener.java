package cn.pyg.search.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author heshuangjun
 * @date 2018-11-11 18:26
 */
public class DeleItemSolrMessageListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        try {
            //同步上架的商品数据到索引库
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();

            SolrDataQuery query = new SimpleQuery("item_goodsid");
            solrTemplate.delete(query);
            solrTemplate.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
