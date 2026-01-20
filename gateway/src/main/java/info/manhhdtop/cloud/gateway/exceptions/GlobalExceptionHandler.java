//package info.manhhdtop.cloud.gateway.exceptions;
//
//import info.manhhdtop.cloud.common.dtos.ApiResponse;
//import info.manhhdtop.cloud.common.exceptions.AccessDeninedException;
//import info.manhhdtop.cloud.common.exceptions.AccessTokenExpiredException;
//import info.manhhdtop.cloud.common.exceptions.UnauthorizedException;
//import info.manhhdtop.cloud.common.utils.JsonUtil;
//import org.jspecify.annotations.NullMarked;
//import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//@RestControllerAdvice
//public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
//    @Override
//    @NullMarked
//    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//        var responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        if (ex instanceof UnauthorizedException || ex instanceof AccessTokenExpiredException) {
//            responseStatus = HttpStatus.UNAUTHORIZED;
//        } else {
//            if (ex instanceof AccessDeninedException) {
//                responseStatus = HttpStatus.FORBIDDEN;
//            }
//        }
//        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        var apiResponse = ApiResponse.error(responseStatus.value(), ex.getMessage());
//        String msg = JsonUtil.toJson(apiResponse);
//        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
//
//        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//        exchange.getResponse().getHeaders()
//                .add("Content-Type", "application/json");
//
//        return exchange.getResponse().writeWith(Mono.just(buffer));
//    }
//}
//
