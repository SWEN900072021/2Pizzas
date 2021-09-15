package com.twopizzas.web;

import com.twopizzas.di.Controller;
import lombok.Data;
import lombok.experimental.Accessors;

@Controller
public class TestController {

    @RequestMapping(
            path = "/hello/{hello}",
            method = HttpMethod.POST
    )
    RestResponse<ResponseDto> sayHello(@PathVariable("hello") String hello, @RequestBody RequestDto body) {
        return RestResponse.ok(new ResponseDto().setSayBack("you said: " + body.getSay()));
    }

    @RequestMapping(
            path = "/hello/{hello}",
            method = HttpMethod.GET
    )
    RestResponse<ResponseDto> sayHello(@PathVariable("hello") String hello, @QueryParameter("hello") String helloQuery) {
        return RestResponse.ok(new ResponseDto().setSayBack("you got: " + hello + ", with query: " + helloQuery));
    }

    @Data
    public static class RequestDto {
        String say;
    }

    @Data
    @Accessors(chain = true)
    public static class ResponseDto {
        String sayBack;
    }
}
