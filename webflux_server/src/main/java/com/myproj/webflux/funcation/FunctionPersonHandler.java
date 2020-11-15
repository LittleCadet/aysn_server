package com.myproj.webflux.funcation;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenxie
 * @date 2020/11/15
 */
//public class FunctionPersonHandler {
//
//    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
//
//    public Mono<ServerResponse> getListV1(ServerRequest serverRequest){
//        Flux<String> personList = Flux.just("张三" , "李四" , "王二麻子").publishOn(Schedulers.fromExecutor(EXECUTOR)).map(a -> {
//            System.out.println(Thread.currentThread().getName());
//            return a;
//        });
//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(personList,String.class);
//    }
//
//    public Mono<ServerResponse> getListV2(ServerRequest serverRequest){
//        Mono<String> personList = Mono.just("张三" ).publishOn(Schedulers.fromExecutor(EXECUTOR)).map(a -> {
//            System.out.println(Thread.currentThread().getName());
//            return a;
//        });
//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(personList,String.class);
//    }
//}
