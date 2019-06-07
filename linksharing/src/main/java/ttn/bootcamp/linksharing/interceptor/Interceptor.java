package ttn.bootcamp.linksharing.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class Interceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    //Boolean isLogout = false;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("interceptor url : " + request.getRequestURI());
        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            User user1 = userService.getUserByUserId(user.getUserId());
            //System.out.println("active>>>>>>>>>>>"+user1.isActive());
            if (!user1.isActive()) {
                session.invalidate();
                response.sendRedirect("/");
                return false;
            }
        }


        if(session.getAttribute("user") == null){
            response.sendRedirect("/");
            return false;
        }
        return true;


    }


    /*public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getRequestURI().equals("/logout"))
            isLogout = true;
        return true;

    }*/
}