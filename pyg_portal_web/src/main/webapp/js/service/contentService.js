app.service('contentService', function ($http) {


    /**
     * 根据广告分类的id查询广告数据
     */
    this.findByCategoryId = function (categoryId) {
        //这里get("../")不用..要注意index.html 和之前的admin下的html页面相对webapp目录的路径是不一样的额!
        return $http.get("content/findByCategoryId.do?categoryId=" + categoryId);
    }


});
