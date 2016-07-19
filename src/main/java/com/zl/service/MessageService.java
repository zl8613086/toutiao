package com.zl.service;

import com.zl.dao.MessageDao;
import com.zl.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zl on 2016/7/17.
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;
    public int addMessage(Message message){
        return messageDao.addMessage(message);
    }
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }
    public int getConversationUnreadCount(int userId,String conversationId ){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }
}
