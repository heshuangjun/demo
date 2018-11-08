package cn.pyg.sellergoods.service;
import cn.pyg.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface BrandService {

	/**
	 * 查询全部
	 */
	public List<TbBrand> findAll();
	
	
	/**
	 * 按分页查询(包含了查询全部方法,分析下面)
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加品牌
	*/
	public void add(TbBrand brand);
	
	
	/**
	 * 修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
	 */
	public void update(TbBrand brand);
	

	/**
	 * 修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
	 */
	public TbBrand findOne(Long id);
	
	
	/**
	 * 批量删除
	 */
	public void delete(Long[] ids);

	/**
	 * 条件分页查询(模糊查询)
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 */
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

	//查询模板关联的下拉列表数据
	public List<Map> selectOptionList();
}
