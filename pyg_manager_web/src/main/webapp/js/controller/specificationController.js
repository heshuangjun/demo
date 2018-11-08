/**
 *  定义控制层:Controller,主要是负责封装和处理页面以及后端的数据
 */
app.controller("specificationController", function ($scope, $controller, specificationService) {


    //继承的写法
    $controller("baseController", {$scope: $scope});  //共享scope


    /**
     * 查询所有
     */
    $scope.findAll = function () {
        specificationService.findAll().success(function (response) {
            $scope.list = response;
        })
    }

    // /**
    //  * 完成分页(前+后端)
    //  */
    // $scope.reloadList = function () {
    //     $http.get("../brand/findPage.do?pageNumber=" + $scope.paginationConf.currentPage + "&pageSize=" + $scope.paginationConf.itemsPerPage).success(function (response) {
    //         //response {long total,rows=[{},{},{}]}
    //         $scope.paginationConf.totalItems = response.total;//显示总的记录数
    //         $scope.brandList = response.rows; //显示数据brandList
    //     })
    // }

    /**
     * 条件分页查询(模糊查询,根据规格的名称查询之后的结果按分页的形式展示到页面的功能)
     */
    $scope.searchEntity = {};//解决请求参数为空的问题

    $scope.search = function (page, rows) {
        specificationService.search(page, rows, $scope.searchEntity).success(function (response) {
            // alert($scope.searchEntity);
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }

    /**
     * 新建规格+规格选项的功能(这里要注意的是保存规格的同时,根据该规格的id应该把其规格的选项内存也一并保存)
     * 修改规格
     */
    $scope.save = function () {

        var method = null;
        //添加add.do 和修改update.do 判断谁先执行

        if ($scope.entity.tbSpecification.id != null) {
            method = specificationService.update($scope.entity);
        } else {
            method = specificationService.add($scope.entity);
        }
        method.success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        })
    }

    /**
     * 给后端一个id,返回前端页面想要的数据的方法
     */
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    }

    /**
     * 删除功能附加条件:--------更新复选框勾线状态的方法
     */
    $scope.selectIds = [];
    $scope.updateSelection = function ($event, id) {
        //判断复选框勾选的状态 $event.target获取这个操作的checkbox的对象,checked就是该对象的属性:是否选中的属性的意思
        if ($event.target.checked) {
            //如果id到数组
            $scope.selectIds.push(id);
        } else {
            //取消勾选 参数一:移除元素的索引值;参数二:移除几个元素
            //获取id在数组中的索引值index
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);
        }
    }


    /**
     * 删除
     */
    $scope.del = function () {
        if (window.confirm("给老子想清楚了!")) {
            specificationService.del($scope.selectIds).success(function (response) {
                if (response.success) {
                    //删除成功,重新刷新页面数据,掺杂了分页的功能
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            })
        }
    }


    /**
     * 增加和删除规格选项行里面的选项列表
     */
    $scope.entity = {specificationOptions: []};

    $scope.addRow = function () {
        $scope.entity.specificationOptions.push({});
    }

    $scope.deleRow = function (index) {
        $scope.entity.specificationOptions.splice(index, 1);
    }


});