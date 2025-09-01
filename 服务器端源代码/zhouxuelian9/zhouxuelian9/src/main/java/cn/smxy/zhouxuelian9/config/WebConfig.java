package cn.smxy.zhouxuelian9.config;

import cn.smxy.zhouxuelian9.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并设置拦截路径和排除路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/checkToken") // 拦截路径
                .addPathPatterns("/user/order/**") // 拦截路径
                .excludePathPatterns("/user/login"); // 排除路径，即不拦截登录请求
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加资源处理器，用于映射静态资源
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:D:\\upload\\"); // 指定静态资源的路径
    }
}