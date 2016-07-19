package com.zl.controller;

import com.zl.async.EventModel;
import com.zl.async.EventProducer;
import com.zl.async.EventType;
import com.zl.model.EntityType;
import com.zl.model.HostHolder;
import com.zl.model.News;
import com.zl.service.LikeService;
import com.zl.service.NewsService;
import com.zl.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Created by zl on 2016/7/18.
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        long likecount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        News news=newsService.getById(newsId);
        newsService.updateLikeCount(newsId, (int) likecount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJsonString(0,String.valueOf(likecount));
    }
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId) {
        long likecount=likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId, (int) likecount);
        return ToutiaoUtil.getJsonString(0,String.valueOf(likecount));
    }
}
