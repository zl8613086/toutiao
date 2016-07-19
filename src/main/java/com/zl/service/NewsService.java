package com.zl.service;

import com.zl.dao.NewsDao;
import com.zl.model.News;
import com.zl.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by zl on 2016/7/3.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDao newsdao;
    public List<News> getLatestNews(int userId,int offset,int limit){
        return newsdao.selectByUserIdAndOffset(userId, offset, limit);
    }
    public String saveImage(MultipartFile file)throws IOException{
        int dopos=file.getOriginalFilename().indexOf(".");
        if(dopos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(dopos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }

        String filename= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        File tem=new File(ToutiaoUtil.IMAGE_DIR+filename);
        tem.mkdirs();
        Files.copy(file.getInputStream(),tem.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+filename;

    }
    public int addNews(News news){
        newsdao.addNews(news);
        return news.getId();

    }
    public News getById(int id){
        return newsdao.getById(id);
    }
    public int updateCommentCount(int commentCount,int id){
        return newsdao.updateCommentCount(commentCount,id);
    }
    public int updateLikeCount(int id,int likeCount){
        return newsdao.updateLikeCount(id,likeCount);
    }
}
