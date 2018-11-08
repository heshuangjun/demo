//服务层
app.service('brandService',function($http){

    //查询全部品牌数据
	this.findAll=function(){
		return $http.get('../brand/findAll.do');		
	}

    //分页+查询全部品牌数据
	this.findPage=function(page,rows){
		return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
	}

	//增加品牌
	this.add=function(entity){
		return  $http.post('../brand/add.do',entity );
	}

	//修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
	this.findOne=function(id){
		return $http.get('../brand/findOne.do?id='+id);
	}

	//修改品牌数据(点击修改按钮,先要根据id查询该指定的品牌数据展示到页面)
	this.update=function(entity){
		return  $http.post('../brand/update.do',entity );
	}

	//删除
	this.dele=function(ids){
		return $http.get('../brand/delete.do?ids='+ids);
	}

	//条件分页查询(模糊查询)
	this.search=function(page,rows,searchEntity){
		return $http.post('../brand/search.do?page='+page+"&rows="+rows, searchEntity);
	}

    //查询模板关联的下拉列表数据
	this.selectOptionList=function () {
        return $http.get('../brand/selectOptionList.do');
    }
});
