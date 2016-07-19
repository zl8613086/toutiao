package com.zl.controller;

import com.zl.dao.MessageDao;
import com.zl.model.HostHolder;
import com.zl.model.Message;

import com.zl.model.User;
import com.zl.model.ViewObject;
import com.zl.service.MessageService;
import com.zl.service.UserService;
import com.zl.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2016/7/17.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try {
            int localUserId=hostHolder.getUser().getId();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations=new ArrayList<>();
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("conversation",msg);
                int targetId=msg.getFromId()==localUserId? msg.getToId():msg.getFromId();
                User user=userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        } catch (Exception e) {
            logger.info("获取站内信失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
            logger.info("获取消息失败" + e.getMessage());
        }
        return "letterDetail";

    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJsonString(message.getId());
        } catch (Exception e) {
            logger.info("增加评论错误" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "插入评论失败");
        }


    }
}
