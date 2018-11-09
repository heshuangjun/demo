package cn.pyg.page.service;

import groupEntity.Goods;

/**
 * @author heshuangjun
 * @date 2018-11-09 18:05
 */
public interface ItemPageService {
    /**
     * 查询静态化商品数据
     */
    public Goods findOne(Long goodsId);
}
