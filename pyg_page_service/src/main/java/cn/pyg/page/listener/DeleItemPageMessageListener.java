package cn.pyg.page.listener;

import cn.pyg.mapper.TbItemMapper;
import cn.pyg.pojo.TbItem;
import cn.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.util.List;

/**
 * @author heshuangjun
 * @date 2018-11-11 19:40
 */
public class DeleItemPageMessageListener implements MessageListener {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {

        try {
            //同步下架删除的商品静态页
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();

            //根据id查询需要同步的商品数据
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));//将String类型转化成Long类型;
            List<TbItem> itemList = itemMapper.selectByExample(example);

            for (TbItem item : itemList) {
                Long itemId = item.getId();
                //删除文件操作(文件自带删除的方法)
                new File("/Library/apache-tomcat-8.5.33/webapps/page41/"+itemId+".html").delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
