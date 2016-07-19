package com.zl.service;

import com.zl.dao.CommentDao;
import com.zl.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zl on 2016/7/15.
 */
@Service
public class CommentService {
    @Autowired
    private CommentDao commentdao;
     public List<Comment> getCommentByEntity(int entityId,int entityType){
         return commentdao.selectByEntity(entityId,entityType);
     }
    public int addComment(Comment comment){
        return commentdao.addComment(comment);
    }
    public int getCommentCount(int entityId,int entityType){
        return commentdao.getCommentCount(entityId,entityType);
    }
    public void deleteComment(int entityId,int entityType){
        commentdao.updateStatus(entityId,entityType,1);
    }
}
