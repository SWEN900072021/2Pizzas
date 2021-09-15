package com.twopizzas.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twopizzas.data.IdentityMapper;
import com.twopizzas.data.UnitOfWork;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HttpRequestDispatcherImpl implements HttpRequestDispatcher {

    private final WebApplicationContext context;
    private final UnitOfWork unitOfWork;
    private final IdentityMapper identityMapper;

    @Autowired
    public HttpRequestDispatcherImpl(WebApplicationContext context, UnitOfWork unitOfWork, IdentityMapper identityMapper) {
        this.context = context;
        this.unitOfWork = unitOfWork;
        this.identityMapper = identityMapper;
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        List<HttpRequestDelegate> delegates = context.getDelegatesForPath(request.getPath());
        if (delegates.isEmpty()) {
            return buildErrorResponse(request, HttpStatus.NOT_FOUND, null);
        }

        // handle CORS
        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Access-Control-Allow-Origin", request.getHeaders().getOrDefault("origin", "*"));
            headers.put("Access-Control-Allow-Methods", delegates.stream().flatMap(d -> d.getMethods().stream()).map(HttpMethod::name).distinct().collect(Collectors.joining(", ")));
            headers.put("Access-Control-Allow-Headers", request.getHeaders().getOrDefault("access-control-request-headers", "*"));
            return new HttpResponse(
                    HttpStatus.NO_CONTENT, null, headers
            );
        }

        for (HttpRequestDelegate delegate : delegates) {
            if (delegate.getMethods().contains(request.getMethod())) {
                return handle(request, delegate);
            }
        }

        // no delegate for method return error
        return buildErrorResponse(request, HttpStatus.METHOD_NOT_SUPPORTED, null);
    }

    private HttpResponse handle(HttpRequest request, HttpRequestDelegate delegate) {
        try {
            identityMapper.reset();
            unitOfWork.start();
            RestResponse<?> restResponse = delegate.handle(request);

            String bodyStr = null;
            if (restResponse.getBody() != null) {
                if (restResponse.getBody() instanceof String) {
                    bodyStr = (String) restResponse.getBody();
                } else {
                    bodyStr = context.getObjectMapper().writeValueAsString(request.getBody());
                }
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("Access-Control-Allow-Origin", request.getHeaders().getOrDefault("origin", "*"));
            HttpResponse response = new HttpResponse(
                    restResponse.getStatus(),
                    context.getObjectMapper().writeValueAsString(restResponse.getBody()),
                    headers
            );

            unitOfWork.commit();
            return response;

        } catch (Throwable e) {
            unitOfWork.rollback();

            if (e instanceof HttpException) {
                return buildErrorResponse(request, ((HttpException) e).getStatus(), ((HttpException) e).getReason());
            }
            return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private HttpResponse buildErrorResponse(HttpRequest request, HttpStatus status, String reason) {
        String body = null;
        try {
            body = context.getObjectMapper().writeValueAsString(new ErrorResponseDto()
                    .setUrl(request.getPath())
                    .setStatus(status.getStatusCode())
                    .setMessage(status.getStatus())
                    .setReason(reason));
        } catch (JsonProcessingException e) {
            System.out.println("big woops serializing body");
        }

        return new HttpResponse(
                status,
                body,
                new HashMap<>()
        );
    }
}
