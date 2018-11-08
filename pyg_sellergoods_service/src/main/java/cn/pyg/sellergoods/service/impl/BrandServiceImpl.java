package cn.pyg.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.pyg.mapper.TbBrandMapper;
import cn.pyg.pojo.TbBrand;
import cn.pyg.pojo.TbBrandExample;
import cn.pyg.pojo.TbBrandExample.Criteria;
import cn.pyg.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 按分页查询(包含了查询全部方法,分析下面)
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);//brandMapper.selectByExample(null)这一步就是查询了全部的数据
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加品牌
     */
    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }


    /**
     * 修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
     */
    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 条件分页查询(模糊查询)
     * @param brand
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {

        //分页的操作
        PageHelper.startPage(pageNum, pageSize);

        //当前页的记录数据,创建条件查询对象
        TbBrandExample example = new TbBrandExample();
        Criteria criteria = example.createCriteria();

        if (brand != null) {
            if (brand.getName() != null && brand.getName().length() > 0) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
                criteria.andFirstCharLike("%" + brand.getFirstChar() + "%");
            }

        }

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    //查询模板关联的下拉列表数据
    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }

}
