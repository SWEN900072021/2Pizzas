package com.twopizzas;

import com.twopizzas.di.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Collection;

@WebListener
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext applicationContext = ApplicationContext.getInstance()
                .root("someroot")
                .init();

        ServletContext servletContext = sce.getServletContext();
        findServlets().forEach(
               d -> servletContext.addServlet(d.clasz.getName(), applicationContext.getComponent(d.clasz))
                       .addMapping(d.route)
        );
    }

    private Collection<ServletDefinition> findServlets() {
        return null;
    }

    private static class ServletDefinition {
        private final String route;
        private final Class<? extends HelloServlet> clasz;

        private ServletDefinition(String route, Class<? extends HelloServlet> clasz) {
            this.route = route;
            this.clasz = clasz;
        }
    }
}
