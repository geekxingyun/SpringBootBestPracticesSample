package com.xingyun.springbootwithspringsecuritytokensample.interceptor;

import com.google.gson.Gson;
import com.xingyun.springbootwithspringsecuritytokensample.business.user.model.UserInfoEntity;
import com.xingyun.springbootwithspringsecuritytokensample.business.user.service.UserInfoService;
import com.xingyun.springbootwithspringsecuritytokensample.model.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 权限认证拦截器
 * 这里不需要配置@Component 注解 因为XML 里面会配置
 *
 * @author qing-feng.zhao
 */
public class MyTokenAuthorityInterceptor implements HandlerInterceptor {

    @Autowired
    private UserInfoService userInfoService;

    private List<String> excludeUrls;

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    /**
     * 在一个请求进入Controller层方法执行前执行这个方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在这里可以对参数做一些预处理和做一些验证
        String requestPath = request.getServletPath();

        if (excludeUrls.contains(requestPath)) {
            return true;
        } else {
            String token = request.getParameter("token");
            Optional<UserInfoEntity> userInfoEntityOptional = this.userInfoService.verificationToken(token);
            if(userInfoEntityOptional.isPresent()){
                return true;
            }else{
                //登陆权限拦截 返回错误信息
                //设置响应字符编码
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                AppResponse appResponse=new AppResponse();
                appResponse.setResponseCode(403);
                appResponse.setResponseMessage("token不正确,无权限操作");
                appResponse.setResponseData("请求错误的token="+token);
                Gson gson =new Gson();
                out.print(gson.toJson(appResponse));
                return false;
            }
        }
    }


    //返回model前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //Controller执行完毕后，返回之前，可以对request和reponse进行处理
        //如果是前后端没有分离，在进入View层中前执行
    }

    //返回model后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在一个请求处理完毕，即将销毁的时候，执行，可以做一些资源释放之类的工作
    }
}

