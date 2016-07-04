package com.zl.service;

import com.zl.dao.NewsDao;
import com.zl.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
