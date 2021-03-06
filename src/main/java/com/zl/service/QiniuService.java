package com.zl.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zl.controller.IndexController;
import com.zl.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by zl on 2016/7/10.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "wksQFHCT92F40TSjTon6QCoy47oC2JL4qvyGlVLt";
    String SECRET_KEY = "G6uUE-CEnlfRmNB6r2UnsLaBDt12g3KENXeys7OL";
    //要上传的空间
    String bucketname = "zlstatic";
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dopos = file.getOriginalFilename().indexOf(".");
            if (dopos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dopos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }
            String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), filename, getUpToken());
            //打印返回的信息
           // System.out.println(res.bodyString());
            if(res.isOK()&&res.isJson()){
                String key= (String) JSONObject.parseObject(res.bodyString()).get("key");
                return ToutiaoUtil.QINIU_DOMAIN_PREFIX+key;
            }else{
                logger.info("七牛异常"+res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.info("七牛异常" + e.getMessage());
            return null;
        }

    }
}
