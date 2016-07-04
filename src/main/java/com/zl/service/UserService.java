package com.zl.service;

import com.zl.dao.UserDao;
import com.zl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zl on 2016/7/3.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userdao;
    public User getUser(int id){
        return userdao.selectById(id);
    }


}
