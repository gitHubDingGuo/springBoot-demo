package top.javahouse.session.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
@WebFilter两种使用方法和失效解决方案
1 Filter 实现类 + @WebFilter + @ServletComponentScan：正常urlpattern的Filter
2 Filter 实现类 + FilterRegistrationBean构造 + @Configuration：正常urlpattern的Filter，并且可以设置Order
3 仅仅 Filter实现类 + @Component ：会注册一个urlpattern 为 /*的过滤器
通过1、2、3可以得知，只要在Filter实现类上增加 @Component ，
就会导致 1个正常urlpattern的filter+1个拦截/*的filter（由于@Component注解导致）
————————————————
原文链接：https://blog.csdn.net/z69183787/article/details/127808802
*/

// urlPatterns 要以/开头，如果urlPatterns和controller的路由相同，配置filterChain.doFilter 可以开启到controller那里
@Slf4j
@WebFilter(filterName = "loginFilter",urlPatterns = "/login")
@Order(1)
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //登录以后，后台生成一个session,返回给前端，前端保存本地，每次请求之前放到请求头里面，在请求后台
        //现在是直接拿浏览器生成的
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;
        HttpSession session =httpServletRequest.getSession();
        System.out.println("filter-session="+session.getId());
        //2.chain.doFilter将请求转发给过滤器链下一个filter , 如果没有filter那就是你请求的资源
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
