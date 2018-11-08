package cn.pyg.manager.controller;

import cn.pyg.pojo.TbSpecification;
import cn.pyg.sellergoods.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import groupEntity.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;


    /**
     * 条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
     */
    @RequestMapping("/search")
    public PageResult search(int pageNumber, int pageSize, @RequestBody TbSpecification specification) {
        return specificationService.search(pageNumber, pageSize, specification);
    }



    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Specification specification) {
        //用结果集封装
        try {
            specificationService.add(specification);
            return new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前
     */
    @RequestMapping("/findById")
    public Specification findById(Long id) {
        return specificationService.findById(id);
    }

    /**
     * 根据id查询该处于修改状态的原数据展示到页面当前再进行修改操作
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Specification specification) {
        try {
            specificationService.update(specification);
            return new Result(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }

    /**
     * 删除规格
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            specificationService.delete(ids);
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }



    /**
     * 查询模板关联的规格的下拉列表的数据
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return specificationService.selectOptionList();
    }

}
