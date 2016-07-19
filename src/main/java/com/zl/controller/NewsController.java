package com.zl.controller;

import com.zl.model.*;
import com.zl.service.*;
import com.zl.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2016/7/10.
 */
@Controller
public class NewsController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsservice;
    @Autowired
    UserService userService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @RequestMapping(path={"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId")int newsId,Model model){
        News news=newsservice.getById(newsId);
        int localUserId=hostHolder.getUser().getId();
        if(news!=null){
            if(localUserId!=0){
                model.addAttribute("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                model.addAttribute("like",0);
            }
            List<Comment> comments= commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<>();
            for(Comment comment:comments){
                ViewObject vo=new ViewObject();
                vo.set("comment",comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments",commentVOs);
        }
        User user= userService.getUser(news.getUserId());
        model.addAttribute("news",news);
        model.addAttribute("owner",user);
        return "detail";
    }
    @RequestMapping(path={"/addComment"},method={RequestMethod.POST})
    public String addComment(@RequestParam("newsId")int newsId,
                           @RequestParam("content")String content){
        try{
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            comment.setCreatedDate(new Date());
            commentService.addComment(comment);
            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsservice.updateCommentCount(count, comment.getEntityId());
        }catch (Exception e){
            logger.error("增加评论错误"+e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);

    }

    @RequestMapping(path={"/image"},method={RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName,
                         HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        }catch(Exception e){
            logger.info("读取图片错误"+e.getMessage());
        }
    }

    @RequestMapping(path={"/uploadImage/"},method={RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file){
        try{
            //String fileUrl=newsservice.saveImage(file);
            String fileUrl= qiniuService.saveImage(file);
            if(fileUrl==null){
                return  ToutiaoUtil.getJsonString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJsonString(0,fileUrl);

        }catch(Exception e){
            logger.info("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJsonString(1,"上传图片失败");
        }

    }
    @RequestMapping(path={"/user/addNews/"},method={RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")String image,
                          @RequestParam("title")String title,
                          @RequestParam("link") String link){
        try{
            News news=new News();
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                news.setUserId(0);
            }
            news.setLink(link);
            news.setTitle(title);
            news.setImage(image);
            news.setCreatedDate(new Date());
            newsservice.addNews(news);
            return ToutiaoUtil.getJsonString(0);

        }catch(Exception e){
            logger.error("添加咨询错误"+e.getMessage());
            return ToutiaoUtil.getJsonString(1,"发布失败");
        }


    }

}
