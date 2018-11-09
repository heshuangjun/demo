package cn.pyg.page.service.impl;

import cn.pyg.mapper.TbGoodsDescMapper;
import cn.pyg.mapper.TbGoodsMapper;
import cn.pyg.mapper.TbItemCatMapper;
import cn.pyg.mapper.TbItemMapper;
import cn.pyg.page.service.ItemPageService;
import cn.pyg.pojo.*;
import com.alibaba.dubbo.config.annotation.Service;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private TbItemCatMapper itemCatMapper;


    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();//创建返回的Goods对象

        //封装其属性一  TbGoods
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        goods.setGoods(tbGoods);

        //商品分类名称数据组装
        Map<String, String> itemCatMap = new HashMap<>();
        String itemCat1Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
        String itemCat2Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
        String itemCat3Name = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
        itemCatMap.put("itemCat1Name", itemCat1Name);
        itemCatMap.put("itemCat2Name", itemCat2Name);
        itemCatMap.put("itemCat3Name", itemCat3Name);

        goods.setItemCatMap(itemCatMap);





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
