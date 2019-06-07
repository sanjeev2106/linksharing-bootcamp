package ttn.bootcamp.linksharing.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class NoBrowserCacheFilter /*implements Filter*/ {

    /*@Override
    public void destroy() {

    }*/

   /* @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response=(HttpServletResponse)res;
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        chain.doFilter(req, res);
    }*/

    /*@Override
    public void init(FilterConfig arg0) throws ServletException {

    }*/

}