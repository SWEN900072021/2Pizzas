package com.twopizzas.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class HttpRequest {
    private final String path;
    private final Map<String, String> queries;
    private final String body;
    private final Map<String, String> headers;
    private final HttpMethod method;

    static HttpRequest from(HttpServletRequest request) {
        return new HttpRequest(
                request.getRequestURI().replace(request.getContextPath(), ""),
                parseQueryString(request.getQueryString()),
                readBodyAsString(request),
                parseHeaders(request),
                HttpMethod.valueOf(request.getMethod())
        );
    }

    private static Map<String, String> parseQueryString(String string) {
        Map<String, String> queryMap = new HashMap<>();
        if (string == null) {
            return queryMap;
        }
        String[] queries = string.split("&");
        for (String query : queries) {
            String[] nameAndValue = query.split("=");
            if (nameAndValue.length != 2) {
                throw new RequestProcessingException(String.format("failed to read malformed query [%s] in path", nameAndValue));
            }
            queryMap.put(nameAndValue[0], nameAndValue[1]);
        }
        return queryMap;
    }

    private static Map<String, String> parseHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private static String readBodyAsString(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RequestProcessingException(String.format("failed to read request body as string from servlet reader: %s", e.getMessage()), e);
        }
    }
}
