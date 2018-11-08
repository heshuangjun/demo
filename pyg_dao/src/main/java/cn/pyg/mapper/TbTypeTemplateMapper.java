package cn.pyg.mapper;

import cn.pyg.pojo.TbTypeTemplate;
import cn.pyg.pojo.TbTypeTemplateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbTypeTemplateMapper {
    int countByExample(TbTypeTemplateExample example);

    int deleteByExample(TbTypeTemplateExample example);

    /**
     * 删除
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新建操作
     */
    int insert(TbTypeTemplate record);

    int insertSelective(TbTypeTemplate record);

    /**
     * 查询所有
     */
    List<TbTypeTemplate> selectByExample(TbTypeTemplateExample example);

    /**
     * 根据模板id查询内容
     */
    TbTypeTemplate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbTypeTemplate record, @Param("example") TbTypeTemplateExample example);

    int updateByExample(@Param("record") TbTypeTemplate record, @Param("example") TbTypeTemplateExample example);

    int updateByPrimaryKeySelective(TbTypeTemplate record);

    int updateByPrimaryKey(TbTypeTemplate record);
}