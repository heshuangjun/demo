/**
 *  定义服务层:Service,抽取代码完成,提高代码的复用性,主要是与后端进行交互的
 */
app.service("specificationService", function ($http) {

    //查询所有
    this.findAll = function () {
        return $http.get("../specification/findAll.do");
    };

    //条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post("../specification/search.do?pageNumber=" + pageNum + "&pageSize=" + pageSize, searchEntity);
    }


    //新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
    this.add = function (entity) {
        return $http.post("../specification/add.do", entity);
    }

    //修改规格
    this.update = function (entity) {
        return $http.post("../specification/update.do", entity);
    }

    //修改规格,先查询原来的数据
    this.findOne = function (id) {
        return $http.get("../specification/findById.do?id=" + id);
    }

    //删除规格和规格选项
    this.del = function (ids) {
        return $http.get("../specification/delete.do?ids=" + ids);
    }

    //查询模板关联的规格下拉列表数据
    this.selectOptionList=function () {
        return $http.get('../specification/selectOptionList.do');
    }

});