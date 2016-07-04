package com.zl;

import com.zl.dao.NewsDao;
import com.zl.dao.UserDao;
import com.zl.model.News;
import com.zl.model.User;
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
@Sql("/init-schema.sql")
public class InitData {
	@Autowired
	UserDao userdao;
	@Autowired
	NewsDao newsdao;

	@Test
	public void contextLoads() {
		Random random=new Random();
		for(int i=0;i<11;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userdao.addUser(user);
			News news=new News();
			news.setCommentCount(i);
			Date date=new Date();
			date.setTime(date.getTime()+1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE%d",i));
			news.setLink(String.format("http://www.nowcoder.com/%d.html",i));
			newsdao.addNews(news);


			user.setPassword("ZL");
			userdao.updatePassword(user);
		}
		Assert.assertEquals("ZL",userdao.selectById(1).getPassword());
		userdao.deleteById(1);
		Assert.assertNull(userdao.selectById(1));
	}

}
