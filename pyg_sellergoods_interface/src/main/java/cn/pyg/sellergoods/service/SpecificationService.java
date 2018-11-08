package cn.pyg.sellergoods.service;

import cn.pyg.pojo.TbSpecification;
import entity.PageResult;
import groupEntity.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
    /**
     * 规格的条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
     */
    PageResult search(int pageNumber, int pageSize, TbSpecification specification);

    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     */
    void add(Specification specification);

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前
     */
    Specification findById(Long id);

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前再进行修改操作
     */
    void update(Specification specification);

    /**
     * 删除规格
     */
    void delete(Long[] ids);

    //查询模板关联的规格的下拉列表的数据
    List<Map> selectOptionList();

}
