package com.twopizzas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@ExtendWith(MockitoExtension.class)
public class HelloServletTests {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private HelloServlet servlet;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);

        servlet = new HelloServlet(new MyHelloComponentImpl());
        servlet.init();
    }

    @Test
    @DisplayName("GIVEN valid request WHEN doGet invoked THEN returns correct html")
    void givenValidRequest_whenGet_thenReturnsHelloHtml() throws IOException {
        // GIVEN
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Mockito.when(response.getWriter()).thenReturn(pw);

        // WHEN
        servlet.doGet(request, response);

        // THEN
        String expectedResponseHtml =
                "<html><body>" +
                    "<h1>Hello World!</h1>" +
                "</body></html>";

        Mockito.verify(response).setContentType(Mockito.eq("text/html"));
        Assertions.assertEquals(sw.getBuffer().toString().trim(), expectedResponseHtml);
    }
}
