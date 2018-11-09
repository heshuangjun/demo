package cn.pyg.page.service.impl;

import cn.pyg.mapper.TbGoodsDescMapper;
import cn.pyg.mapper.TbGoodsMapper;
import cn.pyg.mapper.TbItemMapper;
import cn.pyg.page.service.ItemPageService;
import cn.pyg.pojo.TbGoods;
import cn.pyg.pojo.TbGoodsDesc;
import cn.pyg.pojo.TbItem;
import cn.pyg.pojo.TbItemExample;
import com.alibaba.dubbo.config.annotation.Service;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author heshuangjun
 * @date 2018-11-09 18:07
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;


    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();//创建返回的Goods对象

        //封装其属性一  TbGoods
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        goods.setGoods(tbGoods);
        //封装其属性二  TbGoodsDesc
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setGoodsDesc(tbGoodsDesc);
        //封装其属性三  List<TbItem>
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        //返回封装的结果
        goods.setItemList(itemList);

        return goods;
    }
}
