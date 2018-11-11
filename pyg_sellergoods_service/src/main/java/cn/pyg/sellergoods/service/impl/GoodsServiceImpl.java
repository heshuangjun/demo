package cn.pyg.sellergoods.service.impl;

import cn.pyg.mapper.*;
import cn.pyg.pojo.*;
import cn.pyg.pojo.TbGoodsExample.Criteria;
import cn.pyg.sellergoods.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbSellerMapper sellerMapper;
    @Autowired
    private TbItemMapper itemMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 增加
     */

    @Override
    public void add(Goods goods) {

        //保存tb_goods表
        TbGoods tbGoods = goods.getGoods();
        //tbGoods.setAuditStatus("0");
        goodsMapper.insert(tbGoods);

        //设置刚录入的商品的状态,应该是"未审核"状态的
        tbGoods.setAuditStatus("0");
        //赋值完,需要做更新处理!!!!!!!!!!!!!!!!!!!!!!!!
        goodsMapper.updateByPrimaryKey(tbGoods);


        //保存tb_goodsDesc表(描述肯定是基于响应的商品去描述,那么这里需要返回指定商品的id来确定被描述的对象.)
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();
        goodsDesc.setGoodsId(tbGoods.getId());
        goodsDescMapper.insert(goodsDesc);


        if ("1".equals(tbGoods.getIsEnableSpec())) {//启用规格
            //组装sku列表的数据(页面组装的数据都在itemsList中)
            List<TbItem> itemList = goods.getItemList();

            for (TbItem item : itemList) {
                //一:标题的组装规格:商品名称+规格选项名称 以空格分割
                //需要后端组装的数据
                String title = tbGoods.getGoodsName();

                String spec = item.getSpec();
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                for (String key : map.keySet()) {
                    title += " " + map.get(key);
                }
                item.setTitle(title);
                /**
                 * 抽取
                 */
                setItemValue(tbGoods, goodsDesc, item);

                //保存一份
                itemMapper.insert(item);
            }
        } else {
            //没有启用规格,需要制定默认的sku数据,需要去组装item表中的数据
            TbItem item = new TbItem();
            //自己组装的title,没有规格选项名称了
            item.setTitle(tbGoods.getGoodsName());
            //自己组装的price
            item.setPrice(tbGoods.getPrice());
            //自己组装 库存
            item.setNum(99999);
            //自己组装 状态
            item.setStatus("1");
            //自己组装 是否默认
            item.setIsDefault("1");
            /**
             * 抽取
             */
            setItemValue(tbGoods, goodsDesc, item);

            itemMapper.insert(item);

        }
    }

    /**
     * //抽取组装item数据的方法
     */
    private void setItemValue(TbGoods tbGoods, TbGoodsDesc goodsDesc, TbItem item) {
        //二:组装显示封装的图片
        String itemImages = goodsDesc.getItemImages();
        List<Map> imagesList = JSON.parseArray(itemImages, Map.class);
        if (imagesList != null && imagesList.size() > 0) {
            String image = (String) imagesList.get(0).get("url");
            item.setImage(image);
        }
        //三:三级分类的id
        item.setCategoryid(tbGoods.getCategory3Id());
        //四:创建时间
        item.setCreateTime(new Date());
        //五:更新时间
        item.setUpdateTime(new Date());
        //六:goodsId
        item.setGoodsId(tbGoods.getId());
        //七:sellerId
        item.setSellerId(tbGoods.getSellerId());
        //以下字段便于查询
        //八:分类名称
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
        item.setCategory(tbItemCat.getName());
        //九:名牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
        item.setBrand(brand.getName());
        //十:商家的店铺名称
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
        item.setSeller(tbSeller.getNickName());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //逻辑删除
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("1");


            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        //条件查询,还要记住查询未删除状态的商品
        criteria.andIsDeleteIsNull();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //代码生成器生成的模糊查询
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                //这里需要的是等值查询.
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination addItemSolrDestination;

    @Autowired
    private Destination deleItemSolrDestination;


    @Autowired
    private Destination addItemPageDestination;

    @Autowired
    private Destination deleItemPageDestination;

    /**
     * 商品的上下架
     * @param ids
     * @param isMarketable
     */
    @Override
    public void updateIsMarketable(Long[] ids, String isMarketable) {
        for (final Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //这里的上架是指的已审核的商品,需要做一个判断
            if("1".equals(tbGoods.getAuditStatus())){
                //上架
                if("1".equals(isMarketable)){
                    //发送消息,同步添加上架商品到索引库
                    jmsTemplate.send(addItemSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id+"");
                        }
                    });

                    //发送消息,生成相应的静态页
                    jmsTemplate.send(addItemPageDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id+"");
                        }
                    });
                }



                //下架
                if("0".equals(isMarketable)){
                    //发送消息,同步删除上架商品到索引库
                    jmsTemplate.send(deleItemSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id+"");
                        }
                    });

                    //发送消息,同步删除静态页
                    jmsTemplate.send(deleItemPageDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(id+"");
                        }
                    });
                }



                //审核通过
                tbGoods.setIsMarketable(isMarketable);
                goodsMapper.updateByPrimaryKey(tbGoods);
            }else{
                //有审核不通过的,作出提示!
                throw new RuntimeException("只有审核通过的商品才能上下架!");
            }


            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }

}
