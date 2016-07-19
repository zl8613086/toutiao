package com.zl;

import com.zl.dao.CommentDao;
import com.zl.dao.LoginTicketDao;
import com.zl.dao.NewsDao;
import com.zl.dao.UserDao;
import com.zl.model.*;
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
	CommentDao commentdao;
	@Autowired
	NewsDao newsdao;
	@Autowired
	LoginTicketDao loginticketdao;
    @Test
	public void usertest(){
		Random random=new Random();
		User user=new User();
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
		user.setName(String.format("USER%d",11));
		user.setPassword("");
		user.setSalt("");
		userdao.addUser(user);
		int id=user.getId();
		System.out.println(id);
	}
	@Test
	public void contextLoads() {
		Random random=new Random();
		for(int i=0;i<11;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			System.out.println(user.getId());
			userdao.addUser(user);
			System.out.println(user.getId());
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
			for(int s=0;s<3;s++){
				Comment comment=new Comment();
				comment.setUserId(i+1);
				comment.setCreatedDate(new Date());
				comment.setEntityId(news.getId());
				comment.setEntityType(EntityType.ENTITY_NEWS);
				comment.setStatus(0);
				comment.setContent("COMMENT"+s);
				commentdao.addComment(comment);
			}



			user.setPassword("ZL");
			userdao.updatePassword(user);

			LoginTicket ticket=new LoginTicket();
			ticket.setStatus(0);
			ticket.setUserId(i+1);
			ticket.setExpired(date);
			ticket.setTicket(String.format("TICKET%d",i));
			loginticketdao.addTicket(ticket);
			loginticketdao.updateStatus(ticket.getTicket(),2);

		}
		Assert.assertEquals("ZL",userdao.selectById(1).getPassword());
		userdao.deleteById(1);
		Assert.assertNull(userdao.selectById(1));
	}

}
