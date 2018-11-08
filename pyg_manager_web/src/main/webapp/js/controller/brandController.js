//控制层
app.controller('brandController', function ($scope, $controller, brandService) {

    $controller('baseController', {$scope: $scope});//继承

    //查询全部品牌数据
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页+查询全部品牌数据
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;//更新总记录数
                $scope.list = response.rows;//返回到页面的所有的数据
            }
        );
    }

    //修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //增加品牌 或者 保存品牌
    $scope.save = function () {

        var serviceObject;//定义的服务层对象,为了控制两个方法选择其一的实行

        if ($scope.entity.id != null) {//如果有ID,就执行修改
            serviceObject = brandService.update($scope.entity);
        } else {//如果没有ID,就执行新增
            serviceObject = brandService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //增加品牌 或者 保存品牌 成功之后,重新加载
                    $scope.reloadList();//reloadList将走分页查询所有的方法去重新排版整个页面的所有数据
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        if (window.confirm("给老子想清楚,你执行的删除操作!")) {
            //获取选中的复选框
            brandService.dele($scope.selectIds).success(//把定义的更新复选框的id集合当做参数传过去
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//删除成功之后调用分页里面的查询总记录数和当前页结果的方法来刷新页面
                    }
                }
            );
        }
    }

    $scope.searchEntity = {};//定义搜索对象

    //条件分页查询(模糊查询)
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//给列表赋值数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

});	
