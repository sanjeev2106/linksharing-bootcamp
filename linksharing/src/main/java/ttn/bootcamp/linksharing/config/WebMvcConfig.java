package ttn.bootcamp.linksharing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ttn.bootcamp.linksharing.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    Interceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludeURLs = new ArrayList<>();
        excludeURLs.add("/");
        excludeURLs.add("/login");
        excludeURLs.add("/users/registration");
        excludeURLs.add("/forgotPassword");
        excludeURLs.add("/otpCheck");
        excludeURLs.add("/resetPassword");
        excludeURLs.add("/checkEmailExistsInSystemForgetPwd");
        excludeURLs.add("/checkEmailAvailability");
        excludeURLs.add("/checkUsernameAvailability");
        excludeURLs.add("/forgotPasswordPage");
        excludeURLs.add("/postRating/**");
        excludeURLs.add("/css/**");
        excludeURLs.add("/js/**");
        excludeURLs.add("/photos/**");
        excludeURLs.add("/images/**");


        registry.addInterceptor(myInterceptor).excludePathPatterns(excludeURLs);
    }
}

