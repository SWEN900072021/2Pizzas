package com.twopizzas.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public interface WebApplicationContext {
    List<HttpRequestDelegate> getDelegatesForPath(String path);
    ObjectMapper getObjectMapper();
}
