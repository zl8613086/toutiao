package com.zl.interceptor;

import com.zl.dao.LoginTicketDao;
import com.zl.dao.UserDao;
import com.zl.model.HostHolder;
import com.zl.model.LoginTicket;
import com.zl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zl on 2016/7/6.
 */
@Component
public class PassPortInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginTicketDao loginticketdao;
    @Autowired
    private UserDao userdao;
    @Autowired
    HostHolder hostholder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){
            LoginTicket loginticket=loginticketdao.selectByTicket(ticket);
            if(loginticket==null||loginticket.getExpired().before(new Date())|| loginticket.getStatus() != 0){
                return true;
            }
            User user=userdao.selectById(loginticket.getUserId());
            hostholder.setUser(user);

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&hostholder.getUser()!=null){
            modelAndView.addObject("user",hostholder.getUser());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
       hostholder.clear();
    }
}
