package com.twopizzas.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopizzas.data.IdentityMapper;
import com.twopizzas.data.UnitOfWork;
import com.twopizzas.port.data.OptimisticLockingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

public class HttpRequestDispatcherImplTests {

    private HttpRequestDispatcher dispatcher;

    @Mock
    private UnitOfWork unitOfWork;

    @Mock
    private IdentityMapper identityMapper;

    @Mock
    private WebApplicationContext webApplicationContext;

    @Mock
    private HttpRequestDelegate delegate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        dispatcher = new HttpRequestDispatcherImpl(webApplicationContext, unitOfWork, identityMapper);

        Mockito.when(webApplicationContext.getObjectMapper()).thenReturn(new ObjectMapper());
        Mockito.when(webApplicationContext.getDelegatesForPath(Mockito.any())).thenReturn(Collections.singletonList(delegate));
    }

    @Test
    @DisplayName("GIVEN request fails with OptimisticLockingException WHEN dispatch THEN request retried correct number of times")
    void test() throws Throwable {
        // GIVEN
        Mockito.when(delegate.handle(Mockito.any())).thenThrow(new OptimisticLockingException());

        HttpRequest request = new HttpRequest(
                "somePath", Collections.emptyMap(), "{}", Collections.emptyMap(), HttpMethod.POST);
        Mockito.when(delegate.getMethods()).thenReturn(Collections.singleton(request.getMethod()));

                // WHEN
        HttpResponse response = dispatcher.dispatch(request);

        // THEN
        Mockito.verify(delegate, Mockito.times(HttpRequestDispatcherImpl.MAX_RETRIES)).handle(Mockito.eq(request));
        Mockito.verify(identityMapper, Mockito.times(HttpRequestDispatcherImpl.MAX_RETRIES)).reset();
        Mockito.verify(unitOfWork, Mockito.times(HttpRequestDispatcherImpl.MAX_RETRIES)).start();
        Mockito.verify(unitOfWork, Mockito.times(HttpRequestDispatcherImpl.MAX_RETRIES)).rollback();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    }
}
