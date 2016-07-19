package com.zl.dao;

import com.zl.model.Comment;
import com.zl.model.Message;
import org.apache.ibatis.annotations.*;

import javax.annotation.ManagedBean;
import java.util.List;

/**
 * Created by zl on 2016/7/17.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read,conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,@Param("offset")int offset,@Param("limit")int limit);

    @Select({"select ",SELECT_FIELDS,",count(id) as extra1 from(select * from ",TABLE_NAME,"where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId")int userId,
                                      @Param("offset")int offset,
                                      @Param("limit")int limit);

    @Select({"select count(id) from ",TABLE_NAME,"where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId")int userId,@Param("conversationId")String conversationId );
}
