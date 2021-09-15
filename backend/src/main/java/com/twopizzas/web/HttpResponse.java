package com.twopizzas.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Getter
public class HttpResponse {
    private HttpStatus status;
    private String body;
    private Map<String, String> headers;

    void send(HttpServletResponse response) {
        try {
            if (body != null) {
                response.getWriter().print(body);
            }
            headers.forEach(response::setHeader);
            response.setStatus(status.getStatusCode());
        } catch (IOException e) {
            throw new RequestProcessingException(String.format("failed to send response via %s: %s", response.getClass(), e.getMessage()), e);
        }
    }
}
