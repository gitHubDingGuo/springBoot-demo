package top.javahouse.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/*
@WebFilter常用属性

          属性	类型	是否必需	说明
        asyncSupported	boolean	否	指定Filter是否支持异步模式
        dispatcherTypes	DispatcherType[]	否	指定Filter对哪种方式的请求进行过滤。
        支持的属性：ASYNC、ERROR、FORWARD、INCLUDE、REQUEST；
        默认过滤所有方式的请求
        filterName	String	否	Filter名称
        initParams	WebInitParam[]	否	配置参数
        displayName	String	否	Filter显示名
        servletNames	String[]	否	指定对哪些Servlet进行过滤
        urlPatterns/value	String[]	否	两个属性作用相同，指定拦截的路径
        ————————————————
        版权声明：本文为CSDN博主「With_Her」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/With_Her/article/details/82627620*/
@Slf4j
//@Component//无需添加此注解，在启动类添加@ServletComponentScan注解后，会自动将带有@WebFilter的注解进行注入！
/*filterName的首字母一定要小写！！！小写！！！小写！！！
我因为这个，导致配置的多个过滤器拦截url都失效了！不管啥路径，全给我拦截到Filter里去了*/
@WebFilter(filterName = "hubBaseFilter", urlPatterns = "/cas/login")
public class HubBaseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter-init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter-doFilter");
       /* public  void update(UserLoginDTO userLoginDTO){
            redisRepository.setExpire(SESSION_CACHE_PREFIX + userLoginDTO.getSessionId(),
                    userLoginDTO,EXPIRE);
        }*/
    }

    @Override
    public void destroy() {
        System.out.println("Filter-doFilter");
    }


}