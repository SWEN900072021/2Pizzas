package com.twopizzas;

import com.twopizzas.data.UnitOfWork;
import com.twopizzas.di.Autowired;
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

    private MyHelloComponent myHelloComponent;
    private UnitOfWork unitOfWork;

    @Autowired
    HelloServlet(MyHelloComponent myHelloComponent, UnitOfWork unitOfWork) {
        super();
        this.myHelloComponent = myHelloComponent;
        this.unitOfWork = unitOfWork;
    }

    private String message;

    public void init() {
        message = "Hello World!";
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello 
        PrintWriter out = response.getWriter();
        out.print("<html><body>");
        out.print("<h1>" + myHelloComponent.getMessage() + "</h1>");
        out.print("</body></html>");

        // make the changes
        // fetching from db and then update -> calls to register with unit of work
        

        unitOfWork.commit();
    }

    public void destroy() {
    }
}