package com.loser.backend.club.config;


import com.loser.backend.club.util.ContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpAccessIntercepter extends HandlerInterceptorAdapter {


    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            ModelAndView modelAndView) throws Exception {

        ContextHolder.clear();
        super.postHandle(request, response, handler, modelAndView);
    }


}
