package com.twopizzas.web;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DispatcherServlet implements Servlet {

    private ServletConfig config;
    private final HttpRequestDispatcher dispatcher;

    @Autowired
    public DispatcherServlet(HttpRequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        config = servletConfig;
    }

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        HttpResponse response = dispatcher.dispatch(HttpRequest.from(httpServletRequest));
        response.send(httpServletResponse);
    }

    @Override
    public String getServletInfo() {
        return "dispatcher servlet";
    }

    @Override
    public void destroy() {

    }
}
