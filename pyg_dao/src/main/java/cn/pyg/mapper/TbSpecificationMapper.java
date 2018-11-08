package cn.pyg.mapper;

import cn.pyg.pojo.TbSpecification;
import cn.pyg.pojo.TbSpecificationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbSpecificationMapper {
    int countByExample(TbSpecificationExample example);

    int deleteByExample(TbSpecificationExample example);

    /**
     * 删除规格
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     */
    int insert(TbSpecification record);

    int insertSelective(TbSpecification record);

    /**
     * 条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
     */
    List<TbSpecification> selectByExample(TbSpecificationExample example);

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前
     */
    TbSpecification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbSpecification record, @Param("example") TbSpecificationExample example);

    int updateByExample(@Param("record") TbSpecification record, @Param("example") TbSpecificationExample example);

    int updateByPrimaryKeySelective(TbSpecification record);

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前再进行----修改操作----点击保存又执行添加操作调用页面save方法后端add方法
     */
    int updateByPrimaryKey(TbSpecification record);

    //查询模板关联的规格的下拉列表的数据
    List<Map> selectOptionList();
}