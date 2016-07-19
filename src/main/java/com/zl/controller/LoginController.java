package com.zl.controller;

import com.sun.deploy.net.HttpResponse;
import com.zl.async.EventModel;
import com.zl.async.EventProducer;
import com.zl.async.EventType;
import com.zl.model.HostHolder;
import com.zl.model.News;
import com.zl.model.ViewObject;
import com.zl.service.NewsService;
import com.zl.service.UserService;
import com.zl.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2016/7/3.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder  hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/reg/"},method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remember,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0)
                    cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                return ToutiaoUtil.getJsonString(0,"注册成功");
            }else{
                return ToutiaoUtil.getJsonString(1,map);
            }

        }catch (Exception ex){
            logger.info("注册异常"+ex.getMessage());
            return ToutiaoUtil.getJsonString(1,"注册异常");

        }

    }
    @RequestMapping(path={"/login/"},method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remember ,HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0)
                    cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int)map.get("userId"))
                        .setExt("username",username).setExt("email","zl86130862014@163.com"));
                return ToutiaoUtil.getJsonString(0,"登录成功");
            }else{
                return ToutiaoUtil.getJsonString(1,map);
            }

        }catch (Exception ex){
            logger.info("登录异常"+ex.getMessage());
            return ToutiaoUtil.getJsonString(1,"登录异常");

        }

    }
    @RequestMapping(path={"/logout/"},method= {RequestMethod.GET,RequestMethod.POST})

    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


}
