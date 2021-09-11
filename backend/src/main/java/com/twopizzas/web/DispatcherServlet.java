package com.twopizzas.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopizzas.di.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DispatcherServlet implements Servlet {

    ServletConfig config;
    ApplicationContext context;
    List<HttpRequestDelegate> delegates;
    private ObjectMapper mapper = new ObjectMapper();

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
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            dispatch(httpServletRequest, httpServletResponse);
        } catch (HttpException e) {
            servletResponse.setContentType("application/json");
            httpServletResponse.setStatus(e.getStatus().getStatusCode());

            ErrorResponseDto error = new ErrorResponseDto()
                    .setMessage(e.getMessage())
                    .setStatus(e.getStatus().getStatusCode());

            mapper.writeValue(servletResponse.getWriter(), error);
        }
    }

    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws HttpException {
        try {
            for (HttpRequestDelegate delegate : delegates) {
                if (delegate.handle(request, response)) return;
            }
        } catch (HttpException e) {
            throw e;
        } catch (Throwable e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public String getServletInfo() {
        return "dispatcher servlet";
    }

    @Override
    public void destroy() {

    }
}
