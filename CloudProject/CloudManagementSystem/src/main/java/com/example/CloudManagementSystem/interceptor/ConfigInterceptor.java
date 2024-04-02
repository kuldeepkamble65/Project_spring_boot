//package com.example.CloudManagementSystem.interceptor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class ConfigInterceptor implements WebMvcConfigurer {
//
//    @Autowired
//    Interceptor interceptor;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interceptor).addPathPatterns("/user/**").excludePathPatterns("/user/register");
//    }
//}