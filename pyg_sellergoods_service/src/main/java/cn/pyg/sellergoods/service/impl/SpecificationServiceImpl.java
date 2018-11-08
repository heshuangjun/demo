package cn.pyg.sellergoods.service.impl;

import cn.pyg.mapper.TbSpecificationMapper;
import cn.pyg.mapper.TbSpecificationOptionMapper;
import cn.pyg.pojo.TbSpecification;
import cn.pyg.pojo.TbSpecificationExample;
import cn.pyg.pojo.TbSpecificationOption;
import cn.pyg.pojo.TbSpecificationOptionExample;
import cn.pyg.sellergoods.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import groupEntity.Specification;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    /**
     * 条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
     */
    @Override
    public PageResult search(int pageNumber, int pageSize, TbSpecification specification) {
        //设置分页条件
        PageHelper.startPage(pageNumber, pageSize);

        //条件对象
        TbSpecificationExample example = new TbSpecificationExample();
        //封装条件对象
        TbSpecificationExample.Criteria criteria = example.createCriteria();

        if (specification != null) {
            //获取规格的名称
            String specName = specification.getSpecName();
            //判断
            if (specName != null && !"".equals(specName)) {
                criteria.andSpecNameLike("%" + specName + "%");
            }
        }

        Page page = (Page) specificationMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult()); //总记录条数,和当页的数据
    }

    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     */
    @Override
    public void add(Specification specification) {
        //获取规格的数据
        TbSpecification tbSpecification = specification.getTbSpecification();
        //保存规格
        specificationMapper.insert(tbSpecification);

        //获取规格选项
        List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptions();

        //规格选项可能是一条,也可能是添加了多条.应该遍历保存
        for (TbSpecificationOption specificationOption : specificationOptions) {

            //由数据库表得知:规格选项表中specId就是规格表中的Id
            specificationOption.setSpecId(tbSpecification.getId());
            tbSpecificationOptionMapper.insert(specificationOption);
        }
    }

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前
     */
    @Override
    public Specification findById(Long id) {
        //创建出来是为了封装此时此刻查询到的规格和规格选项的内容!!
        Specification specificationNewBean = new Specification();

        //查询规格并存入新封装的对象specificationNewBean,以便返回到页面
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        specificationNewBean.setTbSpecification(tbSpecification);

        //查询规格选项并存入新封装的对象specificationNewBean,以便返回到页面
        //创建条件查询对象
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        //封装条件查询
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);

        List<TbSpecificationOption> tbSpecificationOptions = tbSpecificationOptionMapper.selectByExample(example);
        specificationNewBean.setSpecificationOptions(tbSpecificationOptions);
        return specificationNewBean;
    }

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前再进行----修改操作----点击保存又执行添加操作调用页面save方法后端add方法
     */
    @Override
    public void update(Specification specification) {
        //获得规格对象tbSpecification
        TbSpecification tbSpecification = specification.getTbSpecification();
        specificationMapper.updateByPrimaryKey(tbSpecification);

        //修改规格选项
        //第一步就是删除原来的规格选项
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(tbSpecification.getId());
        //删除原来的规格选项
        tbSpecificationOptionMapper.deleteByExample(example);

        //第二步就是添加新增的规格选项
        List<TbSpecificationOption> specificationOptions = specification.getSpecificationOptions();
        for (TbSpecificationOption specificationOption : specificationOptions) {
            //关联返回的id的操作
            specificationOption.setSpecId(tbSpecification.getId());
            tbSpecificationOptionMapper.insert(specificationOption);
        }
    }

    /**
     * 删除规格
     */
    @Override
    public void delete(Long[] ids) {
        //遍历规格的id
        for (Long id : ids) {
            specificationMapper.deleteByPrimaryKey(id);//删除规格

            //删除规格选项
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            tbSpecificationOptionMapper.deleteByExample(example);
        }

    }

    //查询模板关联的规格的下拉列表的数据
    @Override
    public List<Map> selectOptionList() {
        return specificationMapper.selectOptionList();
    }
}
