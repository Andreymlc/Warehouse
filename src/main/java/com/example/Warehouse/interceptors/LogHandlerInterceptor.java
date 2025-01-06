package com.example.Warehouse.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogHandlerInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LogManager.getLogger(Component.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("Request: {}", request.getRequestURI());
        return true;
    }
}
