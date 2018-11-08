package cn.pyg.mapper;

import cn.pyg.pojo.TbBrand;
import cn.pyg.pojo.TbBrandExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {
    int countByExample(TbBrandExample example);

    int deleteByExample(TbBrandExample example);

    //根据id删除品牌
    int deleteByPrimaryKey(Long id);

    //增加品牌
    int insert(TbBrand record);

    int insertSelective(TbBrand record);

    //查询全部品牌;分页查询全部品牌;
    List<TbBrand> selectByExample(TbBrandExample example);

    //修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
    TbBrand selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbBrand record, @Param("example") TbBrandExample example);

    int updateByExample(@Param("record") TbBrand record, @Param("example") TbBrandExample example);

    int updateByPrimaryKeySelective(TbBrand record);

    //修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
    int updateByPrimaryKey(TbBrand record);

    //查询模板关联的下拉列表数据
    List<Map> selectOptionList();
}