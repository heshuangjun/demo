app.controller('searchController', function ($scope, $controller, searchService) {

    $controller('baseController', {$scope: $scope});//继承baseController


    //从页面的400异常捕捉到问题是从Controller层没有传一个对象,因此先初始此对象,对象格式在html去找定义好的.
    /**
     * 查询条件对象
     */
    $scope.searchMap = {
        keywords: "",
        category: "",
        brand: "",
        price: "",
        spec: {},//规格
        sort: "ASC",//排序规则
        sortField: "",//排序字段

        pageNo:1,
        pageSize:60

    };


    /**
     * 搜索
     */
    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
            //构建分页工具条
            buildPageLabel();
        })
    }


    /**
     * 构建添加条件查询
     */
    $scope.addFilterCondition = function (key, value) {
        if (key == "brand" || key == "category" || key == "price") {
            //key=brand,value='华为'
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }

        //执行查询操作
        $scope.search();
    }

    /**
     * 移除条件对象(页面的删除已选条件的操作)
     */
    $scope.removeSearchItem = function (key) {
        if (key == "brand" || key == "category" || key == "price") {
            //key=brand,value='华为'
            $scope.searchMap[key] = "";
        } else {
            //为规格复杂,delete
            //$scope.searchMap.spec[key]="";这个对象没有清楚,value清空,但是还有key存在,用下面新的删除的api
            delete $scope.searchMap.spec[key];
        }

        //执行查询操作
        $scope.search();
    }

    /**
     * 排序功能的实现
     */
    $scope.sortSearch = function (sortField, sort) {
        //查询和修改的操作
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        //执行查询操作
        $scope.search();
    }












    /**
     * 构建分页工具条代码
     */
    buildPageLabel=function(){
        $scope.pageLabel = [];// 新增分页栏属性
        var maxPageNo = $scope.resultMap.totalPages;// 得到最后页码

        // 定义属性,显示省略号
        $scope.firstDot = true;
        $scope.lastDot = true;

        var firstPage = 1;// 开始页码
        var lastPage = maxPageNo;// 截止页码

        if ($scope.resultMap.totalPages > 5) { // 如果总页数大于5页,显示部分页码
            if ($scope.resultMap.pageNo <= 3) {// 如果当前页小于等于3
                lastPage = 5; // 前5页
                // 前面没有省略号
                $scope.firstDot = false;

            } else if ($scope.searchMap.pageNo >= lastPage - 2) {// 如果当前页大于等于最大页码-2
                firstPage = maxPageNo - 4; // 后5页
                // 后面没有省略号
                $scope.lastDot = false;
            } else {// 显示当前页为中心的5页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            // 页码数小于5页  前后都没有省略号
            $scope.firstDot = false;
            $scope.lastDot = false;
        }
        // 循环产生页码标签
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }


    //分页查询
    $scope.queryForPage=function(pageNo){
        $scope.searchMap.pageNo=pageNo;

        //执行查询操作
        $scope.searchItem();

    }

    //分页页码显示逻辑分析：
    // 1,如果页面数不足5页,展示所有页号
    // 2,如果页码数大于5页
    // 1) 如果展示最前面的5页,后面必须有省略号.....
    // 2) 如果展示是后5页,前面必须有省略号
    // 3) 如果展示是中间5页,前后都有省略号

    // 定义函数,判断是否是第一页
    $scope.isTopPage = function() {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }
    // 定义函数,判断是否最后一页
    $scope.isLastPage = function() {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }

})