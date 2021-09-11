package com.twopizzas.web;

import com.twopizzas.di.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DispatcherServlet implements Servlet {

    ServletConfig config;
    ApplicationContext context;



    public DispatcherServlet(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.config = servletConfig;

    }

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

    }

    @Override
    public String getServletInfo() {
        return "dispatcher servlet";
    }

    @Override
    public void destroy() {

    }
}
