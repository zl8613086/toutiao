package com.zl.controller;

import com.zl.model.User;
import com.zl.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.*;

/**
 * Created by zl on 2016/6/27.
 */
//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(){
        logger.info("visit index");
        return "hello,,,world!!!<br>"+"toutiaosay:"+ toutiaoService.say();
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue="1" )int type,
                          @RequestParam(value="key", defaultValue="zl")String key){
        return String.format("GID{%s},UID{%d},Type{%d},Key{%s}",groupId,userId,type,key);

    }
    @RequestMapping("/vm")
    public String news(Model model){
        model.addAttribute("value1","111");
        List<String> colors= Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        Map<String,String> map=new HashMap<String,String>();
        for(int i=0;i<6;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("Colors",colors);
        model.addAttribute("Map",map);
        model.addAttribute("User",new User("jack"));



        return "news";

    }
    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        StringBuilder builder=new StringBuilder();
        Enumeration<String> headernames=request.getHeaderNames();
        while(headernames.hasMoreElements()){
            String name=headernames.nextElement();
            builder.append(name+":"+request.getHeader(name)+"<br>");
        }
        for(Cookie cookie:request.getCookies()){
            builder.append("COOKIE:");
            builder.append(cookie.getName());
            builder.append(":");
            builder.append(cookie.getValue()+"<br>");
        }
        builder.append("getmethod:"+request.getMethod()+"<br>");
        builder.append("getpathinfo:"+request.getPathInfo()+"<br>");
        builder.append("getQueryString:"+request.getQueryString()+"<br>");
        builder.append("getRequestURI:"+request.getRequestURI()+"<br>");

        return builder.toString();

    }
    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value="zlzlzl",defaultValue = "zl0") String cookievalue,
                           @RequestParam(value="key",defaultValue = "key")String key,
                           @RequestParam(value="value",defaultValue = "value")String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return  "zlID from cookie"+cookievalue;


    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code")int code){
        RedirectView view=new RedirectView("/",true);
        if(code==301){
            view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return view;

    }
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false)String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();

    }
}
