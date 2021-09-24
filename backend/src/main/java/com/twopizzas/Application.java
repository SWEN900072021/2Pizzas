package com.twopizzas;

import com.twopizzas.di.ApplicationContext;
import com.twopizzas.di.ApplicationContextImpl;
import com.twopizzas.web.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContextImpl applicationContext = new ApplicationContextImpl()
                .root("com.twopizzas");

        String profile = System.getProperty("profile");
        if (profile != null) {
            applicationContext.profile(profile);
        }

        applicationContext.init();

        ServletContext servletContext = sce.getServletContext();
        DispatcherServlet dispatcherServlet = applicationContext.getComponent(DispatcherServlet.class);
        servletContext.addServlet(DispatcherServlet.class.getName(), dispatcherServlet)
                       .addMapping("/");

    }
}
