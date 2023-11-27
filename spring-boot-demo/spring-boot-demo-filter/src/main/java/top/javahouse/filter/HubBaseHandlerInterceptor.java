package top.javahouse.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//4.1使用自定义HandlerInterceptor步骤
//
//(1)实现HandlerInterceptor接口。
//
//(2)把自定义HandlerInterceptor接口，注册到InterceptorRegistry中。
@Slf4j
@Component
public class HubBaseHandlerInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("HubBaseHandlerInterceptor-preHandle");
       /*String sessionId = request.getHeader("sessionId");
        UserLoginDTO userInfo = userSessionComponent.getById(sessionId);
        Object referer = HttpSessionUtil.getCurrentSession().getAttribute("referer");
        if( Objects.isNull(userInfo) && !whiteList.contains(request.getRequestURI())){
            HttpSessionUtil.getCurrentSession().setAttribute("referer", request.getHeader("referer"));
            RestResponseDTO restResponseDTO = new RestResponseDTO();
            restResponseDTO.setCode(StoreLocationErrorEnum.LOGIN_4013.getCode());
            restResponseDTO.setMsg(StoreLocationErrorEnum.LOGIN_4013.getMsgCn());
            response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(JsonUtil.toJSONString(restResponseDTO));
            log.info("用户未登录");
            return false;
        }else{ //设置用户登录信息
            SysUserDTO sysUserDTO = new SysUserDTO();
            if(userInfo!=null){
                sysUserDTO.setId(userInfo.getUserId());
                sysUserDTO.setUsername(userInfo.getUserName());
                sysUserDTO.setLoginId(userInfo.getUserId());
                sysUserDTO.setTenantId("-1");
                UserThreadLocal.setUser(userInfo);
            } else{
                sysUserDTO.setId("0");
                sysUserDTO.setUsername("");
                sysUserDTO.setTenantId("-1");
            }
            TenantContextHolder.setSysUser(sysUserDTO);

        }*/
        return true;
    }

    public  void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("HubBaseHandlerInterceptor-postHandle");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("HubBaseHandlerInterceptor-afterCompletion");
    }

}


