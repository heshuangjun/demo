//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            //记录当前分类父id
            $scope.entity.parentId = $scope.parentId;
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findByParentId($scope.parentId);//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        if (window.confirm("要求含有子分类的,是不可以直接删除的!")) {
            //获取选中的复选框
            itemCatService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                    }
                }
            );
        }
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    //添加商品分类的时候,需要知道当前分类的父id
    $scope.parentId = 0;
    //根据上级的ID来查询下级的分类列表
    $scope.findByParentId = function (parentId) {
        $scope.parentId = parentId;
        itemCatService.findByParentId(parentId).success(function (response) {
            $scope.list = response;
        });
    }

    //默认级别为1
    $scope.grade = 1;
    //设置级别
    $scope.setGrade = function (grade) {
        $scope.grade = grade;
    }

    //查询基于面包屑导航栏查询该分类下的子分类
    //为{{entity_2.name}} {{entity_3.name}}赋值
    $scope.selectList = function (p_entity) {
        if ($scope.grade == 1) {
            $scope.entity_2 = null;
            $scope.entity_3 = null;
        }
        if ($scope.grade == 2) {
            $scope.entity_2 = p_entity;
            $scope.entity_3 = null;
        }
        if ($scope.grade == 3) {
            $scope.entity_3 = p_entity;
        }
        //查询下级分类
        $scope.findByParentId(p_entity.id)
    }

    //查询分类关联的模板数据
    $scope.findTypeTemplateList = function () {
        typeTemplateService.findAll().success(function (response) {
            $scope.typeTemplateList = response;
        })
    }


});
