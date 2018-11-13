//品牌控制层
app.controller('baseController', function ($scope) {

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    $scope.selectIds = [];//选中的ID集合

    //更新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        } else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
        }
    }

    //根据对象的key获取value值，然后以逗号格式拼接value数据
    $scope.getValueBykey = function (jsonString, key) {
        /* [{"id":1,"text":"联想"},{"id":3,"text":"三星"},{"id":2,"text":"华为"},
             {"id":5,"text":"OPPO"},{"id":4,"text":"小米"},{"id":9,"text":"苹果"},
             {"id":8,"text":"魅族"},{"id":6,"text":"360"},
             {"id":10,"text":"VIVO"},{"id":11,"text":"诺基亚"},{"id":12,"text":"锤子"}]*/
        var json = JSON.parse(jsonString);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                // {"id":1,"text":"联想"}
                value += "," + json[i][key];
            } else {
                value += json[i][key];
            }
        }
        return value;
    }


    //根据对象的key从数组中获取对象
    /*
            [
                {"attributeName":"网络","attributeValue":["移动3G","移动4G"]},
                {"attributeName":"屏幕尺寸","attributeValue":["6寸","5寸"]},
            ]
    */
    $scope.searchObjectByKey=function (list,key,name) {

        for(var i=0;i<list.length;i++){
            // list[i]={"attributeName":"网络","attributeValue":["移动3G","移动4G"]}
            if(list[i][key]==name){
                return list[i];
            }
        }

        return null;
    }

});	