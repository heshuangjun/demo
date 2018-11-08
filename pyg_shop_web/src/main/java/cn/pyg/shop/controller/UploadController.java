package cn.pyg.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {

        try {
            //获取文件的拓展名,先要获取源名,然后通过截取的手段 .jpg
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //创建FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");

            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            System.out.println(path);

            //拼接返回的url和ip地址,拼成完整的url访问路径
            String imgUrl = FILE_SERVER_URL + path;

            //关键的点,在于平常咱们写上传成功的信息,这里可以用户返回同类型字符串的路径回去
            return new Result(true, imgUrl);

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }



}
