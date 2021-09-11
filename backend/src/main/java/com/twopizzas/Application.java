package com.twopizzas;

import com.twopizzas.di.ApplicationContext;
import com.twopizzas.di.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import java.util.Collection;
import java.util.stream.Collectors;

@WebListener
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext applicationContext = ApplicationContext.getInstance()
                .root("com.twopizzas");

        String profile = System.getProperty("profile");
        if (profile != null) {
            ApplicationContext.getInstance().profile(profile);
        }

        ApplicationContext.getInstance().init();

        ServletContext servletContext = sce.getServletContext();
        findServlets().forEach(
               d -> servletContext.addServlet(d.clasz.getName(), applicationContext.getComponent(d.clasz))
                       .addMapping(d.route)
        );
    }

    private Collection<ServletDefinition> findServlets() {
        Reflections reflections = new Reflections("com.twopizzas", new ConfigurationBuilder()
                .addScanners(
                        new TypeAnnotationsScanner()
                ));

        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .filter(HttpServlet.class::isAssignableFrom).map(c -> {
                    Controller annotation = c.getAnnotation(Controller.class);
                    if (annotation == null) {
                        throw new IllegalStateException("webservlet class is actually not annotated with WebServlet, go figure...");
                    }
                    return new ServletDefinition(annotation.value(), (Class<? extends HttpServlet>) c);
                }
        ).collect(Collectors.toList());

    }

    public static class ServletDefinition {
        private final String route;
        private final Class<? extends HttpServlet> clasz;

        public ServletDefinition(String route, Class<? extends HttpServlet> clasz) {
            this.route = route;
            this.clasz = clasz;
        }
    }
}
