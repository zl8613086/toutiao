package com.zl.async.handler;

import com.zl.async.EventHandler;
import com.zl.async.EventModel;
import com.zl.async.EventType;
import com.zl.model.Message;
import com.zl.service.MessageService;
import com.zl.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zl on 2016/7/19.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }

    @Override
    public void doHandler(EventModel eventModel) {
        Message message=new Message();
        int fromId=15;
        int toId=eventModel.getActorId();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setContent("登录异常");
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
        Map<String,Object> map=new HashMap<>();
        map.put("username",eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"),"登录异常","mails/welcome.html",map);
    }
}
