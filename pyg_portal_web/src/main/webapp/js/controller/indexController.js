app.controller('indexController', function ($scope, $controller, contentService) {

    $controller('baseController', {$scope: $scope});//继承baseController

    /**
     *  根据广告分类的id查询广告的数据列表
     */
    $scope.findContentListByCategoryId = function (categoryId) {

        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList = response;
        })
    }

    /**
     * 为搜索按钮添加点击事件
     */
    $scope.search = function () {
        //angularjs请求地址传参,需要在?前面+一个#(路由传参)
        location.href = "http://127.0.0.1:8084/search.html#?keywords=" + $scope.keywords;
    }

})