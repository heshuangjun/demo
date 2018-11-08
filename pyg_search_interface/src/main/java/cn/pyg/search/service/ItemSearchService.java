package cn.pyg.search.service;

import java.util.Map;

/**
 * @author heshuangjun
 * @date 2018-11-06 18:08
 * 商品搜索服务接口
 */
public interface ItemSearchService {

    //返回数据,经分析有: 当前页的商品列表 ; 分页相关的数据;
    public Map<String,Object> search(Map searchMap);
}
