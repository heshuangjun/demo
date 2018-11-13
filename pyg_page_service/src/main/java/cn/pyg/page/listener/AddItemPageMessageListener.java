package cn.pyg.page.listener;

import cn.pyg.page.service.ItemPageService;
import cn.pyg.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heshuangjun
 * @date 2018-11-11 19:40
 */
public class AddItemPageMessageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Autowired
    private FreeMarkerConfig freemarkerConfig;

    /**
     * 这是第14天 用阿里大于发短信的功能
     * @param message 短信
     */
    @Override
    public void onMessage(Message message) {
        try {
            //同步上架的生成的商品静态页
            TextMessage textMessage = (TextMessage) message;
            String goodsId = textMessage.getText();

            //页面静态化需要的模型数据
            Goods goods = itemPageService.findOne(Long.parseLong(goodsId));
            //1.获取配置信息对象
            Configuration configuration = freemarkerConfig.getConfiguration();
            //2.加载模板数据,创建一个模板对象
            Template template = configuration.getTemplate("item.ftl");
            List<TbItem> itemList = goods.getItemList();

            for (TbItem tbItem : itemList) {
                System.out.println("数据库要录入了tbItem的商品,才进入循环,获取静态页面的id");
                Map map = new HashMap();
                map.put("goods", goods);
                map.put("tbItem", tbItem);

                //3.创建一个输出流对象Writer,一对一生成FileWriter对象,指定生成的文件名
                Writer out = new FileWriter("/Library/apache-tomcat-8.5.33/webapps/page41/" + tbItem.getId() + ".html");
                //4.调用模板对象的process方法输出文件
                template.process(map, out);
                //5.关闭流
                out.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
