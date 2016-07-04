package com.zl.controller;

import com.zl.model.News;
import com.zl.model.ViewObject;
import com.zl.service.NewsService;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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



    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newslist=newsService.getLatestNews(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newslist){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"},method= {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){

        model.addAttribute("vos",getNews(0,0,10));
        return "home";

    }

    @RequestMapping(path={"/user/{userId}"},method= {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId")int userId){

        model.addAttribute("vos",getNews(userId,0,10));
        return "home";

    }
}