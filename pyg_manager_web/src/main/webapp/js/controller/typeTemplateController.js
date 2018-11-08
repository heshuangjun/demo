//控制层
app.controller('typeTemplateController', function ($scope, $controller, typeTemplateService, brandService, specificationService) {

    $controller('baseController', {$scope: $scope});//继承

    /**
     * 查询所有
     */
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        typeTemplateService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    /**
     * 根据模板id查询内容
     */
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //数据库查出来的是字符串的格式,需要转化成json对象才能实现回显到页面
                $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
                $scope.entity.specIds = JSON.parse($scope.entity.specIds);
                $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
            }
        );
    }

    /**
     * 增加/修改/保存/操作
     */
    $scope.save = function () {

        var serviceObject;//服务层对象

        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    /**
     * 批量删除(selectIds是公共使用的复选框功能)
     */
    $scope.dele = function () {
        if (window.confirm("请仔细看清楚,你执行的是删除操作!")) {
            //获取选中的复选框
            typeTemplateService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                    }
                }
            );
        }
    }


    //搜索
    $scope.searchEntity = {};//定义搜索对象
    $scope.search = function (page, rows) {
        typeTemplateService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    //品牌列表数据  first_char  firstChar
    $scope.brandList = {
        data: []
    };
    $scope.selectBrandList = function () {
        brandService.selectOptionList().success(function (response) {
            $scope.brandList.data = response;
        });
    }

    //规格列表数据
    $scope.specList = {
        data: []
    };
    $scope.selectSpecList = function () {
        specificationService.selectOptionList().success(function (response) {
            $scope.specList.data = response;
        });
    }


    /**
     * 拓展属性的增加与删除
     */
    $scope.entity = {customAttributeItems:[]} //初始化对象

    $scope.addRow = function () {
        $scope.entity.customAttributeItems.push({});
    }
    $scope.deleRow = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }
});
