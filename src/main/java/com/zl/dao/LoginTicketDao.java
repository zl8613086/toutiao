package com.zl.dao;

import com.zl.model.LoginTicket;
import com.zl.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * Created by zl on 2016/7/6.
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME="login_ticket";
    String INSERT_FIELDS="user_id,expired,status,ticket";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;


    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);



    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where user_id=#{userId} and status=0"})
    List<LoginTicket> selectByUserId(int userId);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);


}
