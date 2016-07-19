package com.zl.configuration;

import com.zl.interceptor.LoginRequiredInterceptor;
import com.zl.interceptor.PassPortInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zl on 2016/7/6.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassPortInterceptor passportinterceptor;
    @Autowired
    LoginRequiredInterceptor loginrequiredinterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportinterceptor);
        registry.addInterceptor(loginrequiredinterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
