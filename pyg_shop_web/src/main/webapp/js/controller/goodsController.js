//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, itemCatService, typeTemplateService, uploadService) {

    $controller('baseController', {$scope: $scope});//继承


    //初始化一个图片对象
    //$scope.imgEntity={};

    //完成图片上传的功能
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) { //上传成功 ,取出url
                $scope.imgEntity.url = response.message;  //设置文件地址

                //js 文件上传成功之后的清除上传痕迹
                var file = document.getElementById("file");
                //for IE  Opera Safari Chrome
                if (file.outerHTML) {
                    file.outerHTML = file.outerHTML;
                } else {//FF(包括3.5)
                    file.value = "";
                }

            } else {
                alert(response.message);
            }
        });
    }


    //初始化entity对象,有没有定义的变量存在$scope.entity.goodsDesc.itemImages
    $scope.entity = {goods: {}, goodsDesc: {itemImages: [], specificationItems: []}, itemList: []};

    //将图片对象添加到图片列表:
    $scope.addImageEntity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imgEntity);
    }

    //将图片对象从图片列表移除:
    $scope.deleImageEntity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }


    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    /**
     * 添加商家商品信息
     */
    $scope.add = function () {

        //提取正文内容中KindEditor编辑器上的内容(kindEditor中的HTML代码)
        $scope.entity.goodsDesc.introduction = editor.html();

        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {

                    alert("添加成功!哈哈哈");

                    //页面数据保存成功了之后,清空一下页面
                    $scope.entity = {}

                    //清空富文本编辑器
                    editor.html("");
                } else {
                    alert(response.message);
                }
            }
        );
    }

    /**
     * 一级分类下拉选择框
     */
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
        })
    }

    /**
     *  二级分类下拉选择框
     *  $watch方法用于控制某个变量的值 参数一:监控发生变化的模型数据 参数二:就是发生变化之后要做的事,变化前newValue, 变化后oldValue
     */
    $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
        })
    })

    /**
     *  三级分类下拉选择框
     *  $watch方法用于控制某个变量的值 参数一:监控发生变化的模型数据 参数二:就是发生变化之后要做的事,变化前newValue, 变化后oldValue
     */
    $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        })
    })

    /**
     * 基于$watch三级分类查询模板ID  参数一:监控发生变化的模型数据 参数二:就是发生变化之后要做的事,变化前newValue, 变化后oldValue
     */
    $scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.goods.typeTemplateId = response.typeId;
        })
    })

    //基于模板的ID查询模板的数据  参数一:监控发生变化的模型数据 参数二:就是发生变化之后要做的事,变化前newValue, 变化后oldValue
    $scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            //处理一下品牌数据,因为查询到的都是json格式的字符串的数据
            $scope.brandList = JSON.parse(response.brandIds);

            //拓展属性的确定是根据模板的变化而变化的
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);

        })

        /**
         * 查询规格列表,另起的方法
         */
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.SpecList = response;
        })

    })

    //更新规格选项勾选或取消勾选的操作
    $scope.updateSpecAttribute = function ($event, specName, specOptionsName) {
        //判断规格名称是否存在于勾选的规格列表中
        /*
            参数一:勾选的参数的列表:list等于entity.goodsDesc.specificationItems
            参数二:key值就是  list[i] = {"attributeName":"网络","attributeValue":["移动3G","移动4G"]}
         */
        var specObject = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems, "attributeName", specName);

        if (specObject != null) {
            //规格对象存在于勾选的规格列表中
            //还要继续判断这个是勾选还是取消勾选
            //{"attributeName":"网络","attributeValue":["移动3G","移动4G"]}
            if ($event.target.checked) {
                //勾选
                //向规格选项列表中添加规格选项数据
                specObject.attributeValue.push(specOptionsName);
            } else {
                //取消勾选
                //找出该规格选项列表中元素所处的位置(索引),去完成移除操作
                var index = specObject.attributeValue.indexOf(specOptionsName);
                specObject.attributeValue.splice(index, 1);
                //还要考虑你移除元素,是一个不剩,还是有
                if (specObject.attributeValue.length == 0) {
                    //将规格对象从规格列表中移除
                    var index1 = $scope.entity.goodsDesc.specificationItems.indexOf(specObject)
                    $scope.entity.goodsDesc.specificationItems.splice(index1, 1)
                }
            }

        } else {
            //规格列表中不存在勾选的规格数据,添加操作
            $scope.entity.goodsDesc.specificationItems.push({
                "attributeName": specName,
                "attributeValue": [specOptionsName]
            });
        }
    }


//======================================================js的对象特性:动态添加和删除属性===========================================================================================

    //创建sku列表
    $scope.createItemList=function () {

        //初始化sku列表    spec:{"机身内存":"16G","网络":"联通3G"}
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];

        //[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
        var specList = $scope.entity.goodsDesc.specificationItems;

        if(specList.length==0){
            $scope.entity.itemList=[];
        }

        for(var i=0;i<specList.length;i++){
            //动态为item的spec组装数据 specList[i].attributeName="网络"
            $scope.entity.itemList= addColumn($scope.entity.itemList,specList[i].attributeName,specList[i].attributeValue);
        }
    }

    //组装item列表数据  attributeValue":["移动3G","移动4G"]
    addColumn=function (itemList,attributeName,attributeValue) {
        //新的sku列表
        var newItemList=[];

        //spec:{"机身内存":"16G","网络":"联通3G"}
        for(var i=0;i<itemList.length;i++){
            //{spec:{},price:0,num:99999,status:'0',isDefault:'0'}
            var oldSku = itemList[i];

            for(var j=0;j<attributeValue.length;j++){
                //深克隆  先将原有的json格式对象转为json格式字符串，在将json格式的字符串转为json对象
                var newSku= JSON.parse(JSON.stringify(oldSku));
                newSku.spec[attributeName]= attributeValue[j];
                newItemList.push(newSku);
            }
        }
        return newItemList;
    }

    //定义商品状态的数组
    $scope.status=['未审核','已审核','审核未通过','关闭'];

    //初始化分类数组   redis
    $scope.itemCatList=[];

    //查询分类列表，组装展示分类数据的数组
    //分类数据的数组 索引值分类id  该索引对应的数据分类的名称
    $scope.findItemCatList=function () {
        itemCatService.findAll().success(function (response) {

            for(var i=0;i<response.length;i++){
                $scope.itemCatList[response[i].id] = response[i].name;
            }

        })
    }

    $scope.isMarketable=['下架','上架'];

    //定义商品上下架操作
    $scope.updateIsMarketable=function (isMarketable) {
        goodsService.updateIsMarketable($scope.selectIds,isMarketable).success(function (response) {
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        })
    }


});
