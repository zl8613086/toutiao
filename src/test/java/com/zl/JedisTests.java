package com.zl;

import com.zl.dao.CommentDao;
import com.zl.dao.LoginTicketDao;
import com.zl.dao.NewsDao;
import com.zl.dao.UserDao;
import com.zl.model.*;
import com.zl.util.JedisAdapter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@Sql("/init-schema.sql")
public class JedisTests {
	@Autowired
	UserDao userdao;
	@Autowired
	JedisAdapter jedisAdapter;
    @Test
	public  void JedisTest(){
		User user=new User();
		user.setHeadUrl("http://image.nowcode.com/head/100t.png");
		user.setName("Ben");
		user.setSalt("salt");
		user.setPassword("pwd");
		jedisAdapter.setObject("userxx",user);
		User u=jedisAdapter.getObject("userxx",User.class);
		System.out.println(ToStringBuilder.reflectionToString(u));
	}

}
