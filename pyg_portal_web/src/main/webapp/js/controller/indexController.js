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


})