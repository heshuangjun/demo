
app.controller('baseController' ,function($scope){	
	
    //重新加载列表 数据
    $scope.reloadList=function(){
    	//切换页码  
    	$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);	   	
    }
    
	//分页控件配置 
	$scope.paginationConf = {
         currentPage: 1, //当前页码
         totalItems: 10, //总条数
         itemsPerPage: 10,//每页的记录数
         perPageOptions: [10, 20, 30, 40, 50],  //页码选择
         onChange: function(){  //更改页面时触发
        	 $scope.reloadList();//重新加载
     	 }
	}; 
	

	//更新复选
	$scope.selectIds=[];//定义选中ID的集合

	$scope.updateSelection = function($event, id) {
		if($event.target.checked){//如果是被选中,则增加到数组
			$scope.selectIds.push( id);			
		}else{
			var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
		}
	}
	
	//根据对象的key获取value值，然后以逗号格式拼接value数据
	$scope.getValueBykey=function (jsonString,key) {
       /* [{"id":1,"text":"联想"},{"id":3,"text":"三星"},{"id":2,"text":"华为"},
            {"id":5,"text":"OPPO"},{"id":4,"text":"小米"},{"id":9,"text":"苹果"},
            {"id":8,"text":"魅族"},{"id":6,"text":"360"},
            {"id":10,"text":"VIVO"},{"id":11,"text":"诺基亚"},{"id":12,"text":"锤子"}]*/
		var json=JSON.parse(jsonString);
		var value="";
		for(var i=0;i<json.length;i++){
			if(i>0){
                // {"id":1,"text":"联想"}
                value+=","+json[i][key];
			}else{
                value+=json[i][key];
			}
		}
		return value;
    }
	
});	