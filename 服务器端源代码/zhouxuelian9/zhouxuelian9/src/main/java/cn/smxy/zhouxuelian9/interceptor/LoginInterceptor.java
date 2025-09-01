package cn.smxy.zhouxuelian9.interceptor;

import cn.smxy.zhouxuelian9.util.JWTUtil;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 进入方法前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截非拦截
        if(!(handler instanceof HandlerMethod)){
            return true;//放行
        }
        String token = request.getHeader("token");
        System.out.println("LoginInterceptor token=" + token);

        Map<String, Object> map = new HashMap<>();
        if(token == null || "null".equals(token)) {
            map.put("code", 4010);
            map.put("msg", "未登录");
            String jsonstring = new Gson().toJson(map);//将对象转化为json格式
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonstring);
            return false;//拦截了
        }

        try {
            JWTUtil.verifyToken(token);
            return true;//放行
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("code", 4000);
            map.put("msg", "签名不正确");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("code", 4000);
            map.put("msg", "签名算法不正确");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("code", 4000);
            map.put("msg", "token过期");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstring = new Gson().toJson(map);
        response.setContentType("application/json;charset=utf-8");//将返回结果转化为json格式
        response.getWriter().write(jsonstring);
        return false;//拦截了
    }
}