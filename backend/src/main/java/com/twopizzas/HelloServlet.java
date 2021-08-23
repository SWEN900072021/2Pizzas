package com.twopizzas;

import com.twopizzas.di.Component;
import com.twopizzas.di.Controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Controller("/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello 
        PrintWriter out = response.getWriter();
        out.print("<html><body>");
        out.print("<h1>" + message + "</h1>");
        out.print("</body></html>");
    }

    public void destroy() {
    }
}