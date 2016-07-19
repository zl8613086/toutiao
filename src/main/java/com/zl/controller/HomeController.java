package com.zl.controller;

import com.zl.model.EntityType;
import com.zl.model.HostHolder;
import com.zl.model.News;
import com.zl.model.ViewObject;
import com.zl.service.LikeService;
import com.zl.service.NewsService;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zl on 2016/7/3.
 */
@Controller
public class HomeController {

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;


    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newslist=newsService.getLatestNews(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        for(News news:newslist){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }

            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"},method= {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model, @RequestParam(value="pop",defaultValue = "0") int pop){

        model.addAttribute("vos",getNews(0,0,10));
        model.addAttribute("pop",pop);
        return "home";

    }

    @RequestMapping(path={"/user/{userId}"},method= {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId")int userId){

        model.addAttribute("vos",getNews(userId,0,10));
        return "home";

    }
}
