package com.zl.service;

import com.zl.dao.LoginTicketDao;
import com.zl.dao.UserDao;
import com.zl.model.LoginTicket;
import com.zl.model.User;
import com.zl.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zl on 2016/7/3.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userdao;
    @Autowired
    private LoginTicketDao loginticketdao;

    public User getUser(int id){
        return userdao.selectById(id);
    }

    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
       if(StringUtils.isBlank(username)){
           map.put("msgname","用户名不能为空");
           return map;

       }
        if(StringUtils.isBlank(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }
        User user=userdao.selectByName(username);
        if(user!=null){
            map.put("msgname","用户名已被注册");
            return map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userdao.addUser(user);
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;

    }

    public Map<String,Object> login(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;

        }
        if(StringUtils.isBlank(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }
        User user=userdao.selectByName(username);
        if(user==null){
            map.put("msgname","用户名不存在");
            return map;
        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpsw","密码不正确");
            return map;
        }
        List<LoginTicket> list=loginticketdao.selectByUserId(user.getId());
        if(!list.isEmpty()){
            for(LoginTicket tic:list){
                loginticketdao.updateStatus(tic.getTicket(),1);
            }
        }
        map.put("userId", user.getId());

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);


        return map;

    }
    public void logout(String ticket){
        loginticketdao.updateStatus(ticket,1);
    }
    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_",""));
        loginticketdao.addTicket(ticket);
        return ticket.getTicket();

    }



}
