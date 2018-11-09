package cn.pyg.page.controller;

import cn.pyg.page.service.ItemPageService;
import cn.pyg.pojo.TbItem;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heshuangjun
 * @date 2018-11-09 18:04
 */
@RestController
@RequestMapping("/page")
public class ItemPageController {

    @Reference
    private ItemPageService itemPageService;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @RequestMapping("/getItemHtml")
    public String getItemHtml(Long goodsId) {

        try {
            //页面静态化需要的模型数据
            Goods goods = itemPageService.findOne(goodsId);
            //1.获取配置信息对象
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //2.加载模板数据,创建一个模板对象
            Template template = configuration.getTemplate("item.ftl");
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {

                Map map = new HashMap();
                map.put("goods", goods);
                map.put("tbItem", tbItem);

                //3.创建一个输出流对象Writer,一对一生成FileWriter对象,指定生成的文件名
                Writer out = new FileWriter("/Users/heshuangjun/Desktop/page41/" + tbItem.getId() + ".html");
                //4.调用模板对象的process方法输出文件
                template.process(map, out);
                //5.关闭流
                out.close();
            }
            return "success";//生成模板成功给个提示的效果而已
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";//生成模板成功给个提示的效果而已

        }


    }


}
