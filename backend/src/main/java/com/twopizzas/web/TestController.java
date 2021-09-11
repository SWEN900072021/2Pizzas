package com.twopizzas.web;

import com.twopizzas.di.Component;
import lombok.Data;
import lombok.experimental.Accessors;

@Component
public class TestController {

    @RequestMapping(
            path = "/hello/{hello}",
            method = HttpMethod.POST
    )
    RestResponse<ResponseDto> sayHello(@PathVariable String hello, @RequestBody RequestDto body) {
        return RestResponse.ok(new ResponseDto().setSayBack("you said: " + body.getSay()));
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
