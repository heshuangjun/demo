package cn.pyg.mapper;

import cn.pyg.pojo.TbSpecificationOption;
import cn.pyg.pojo.TbSpecificationOptionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbSpecificationOptionMapper {
    int countByExample(TbSpecificationOptionExample example);

    /**
     * 删除规格选项
     */
    int deleteByExample(TbSpecificationOptionExample example);

    int deleteByPrimaryKey(Long id);

    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     */
    int insert(TbSpecificationOption record);

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前
     */
    List<TbSpecificationOption> selectByExample(TbSpecificationOptionExample example);

    int insertSelective(TbSpecificationOption record);

    TbSpecificationOption selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbSpecificationOption record, @Param("example") TbSpecificationOptionExample example);

    int updateByExample(@Param("record") TbSpecificationOption record, @Param("example") TbSpecificationOptionExample example);

    int updateByPrimaryKeySelective(TbSpecificationOption record);

    int updateByPrimaryKey(TbSpecificationOption record);
}