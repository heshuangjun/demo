    app.service("uploadService", function ($http) {

    //文件的上传
    this.uploadFile = function () {

        //HTML5对象  formData
        var formData = new FormData;
        formData.append("file", file.files[0]);

        return $http({
            method: "post",
            url: "../upload/uploadFile.do",
            data: formData,//文件对象
            headers: {'Content-Type': undefined}, //上传文件必须是这个类型,默认格式是:text/plain
            transformRequest: angular.identity //对整个表单进行二进制序列化
        });
    }


});