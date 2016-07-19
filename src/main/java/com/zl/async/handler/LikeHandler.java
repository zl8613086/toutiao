package com.zl.async.handler;

import com.zl.async.EventHandler;
import com.zl.async.EventModel;
import com.zl.async.EventType;
import com.zl.model.Message;
import com.zl.model.User;
import com.zl.service.MessageService;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zl on 2016/7/19.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Override
    public void doHandler(EventModel eventModel) {
        Message message=new Message();
        User user=userService.getUser(eventModel.getActorId());
        int fromId=15;
        int toId=eventModel.getEntityOwnerId();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(fromId < toId? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        message.setCreatedDate(new Date());
        message.setContent("用户"+user.getName()+"赞了你的资讯，http://localhost:8080/news/"+eventModel.getEntityId());
        message.setHasRead(0);
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
