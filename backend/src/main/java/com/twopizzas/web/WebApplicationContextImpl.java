package com.twopizzas.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.twopizzas.auth.AuthenticationProvider;
import com.twopizzas.di.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
class WebApplicationContextImpl implements WebApplicationContext {

    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .registerModule(new JavaTimeModule())
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private List<HttpRequestDelegate> delegates;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    WebApplicationContextImpl(ApplicationContext applicationContext, AuthenticationProvider authenticationProvider) {
        this.applicationContext = applicationContext;
        this.authenticationProvider = authenticationProvider;
    }

    @PostConstruct
    public void init() {
        delegates = loadDelegates();
    }

    List<HttpRequestDelegate> loadDelegates() {
        List<CompositeHttpRequestDelegate> delegates = new ArrayList<>();
        Collection<?> controllers = applicationContext.getComponentsAnnotatedWith(Controller.class);

        controllers.forEach(c -> Arrays.stream(c.getClass().getDeclaredMethods()).forEach(m -> {
            RequestMapping requestMapping = m.getDeclaredAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                m.setAccessible(true);
                HttpRequestDelegate newDelegate = new BaseRequestDelegate(
                        PathResolver.of(requestMapping.path()),
                        requestMapping.method(),
                        c,
                        m,
                        getObjectMapper(),
                        authenticationProvider);

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

    @Override
    public List<HttpRequestDelegate> getDelegatesForPath(String path) {
        return delegates.stream().filter(d -> d.getPathResolver().test(path).isMatch()).collect(Collectors.toList());
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
