package com.twopizzas.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopizzas.di.ApplicationContext;
import com.twopizzas.di.Controller;
import org.reflections.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class WebApplicationContext {

    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    WebApplicationContext(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
    }

    List<HttpRequestDelegate> loadDelegates() {
        List<CompositeHttpRequestDelegate> delegates = new ArrayList<>();
        Collection<?> controllers = applicationContext.getComponentsAnnotatedWith(Controller.class);

        controllers.stream().forEach(c -> ReflectionUtils.getMethods(c.getClass()).forEach(m -> {
            RequestMapping requestMapping = m.getDeclaredAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                HttpRequestDelegate newDelegate = new BaseRequestDelegate(
                        PathResolver.of(requestMapping.path()),
                        requestMapping.method(),
                        c,
                        m,
                        objectMapper
                );

                boolean registered = false;
                Iterator<CompositeHttpRequestDelegate> delegateIterator = delegates.iterator();
                while (!registered && delegateIterator.hasNext()) {
                    CompositeHttpRequestDelegate delegate = delegateIterator.next();
                    if (delegate.handles(newDelegate.getPathResolver())) {
                        delegate.registerDelegate(newDelegate);
                        registered = true;
                    }
                }

                if (!registered) {
                    CompositeHttpRequestDelegate newComposite = new CompositeHttpRequestDelegate();
                    newComposite.registerDelegate(newDelegate);
                    delegates.add(newComposite);
                }
            }
        }));

        return delegates.stream().map(d -> (HttpRequestDelegate) d).collect(Collectors.toList());
    }
}
