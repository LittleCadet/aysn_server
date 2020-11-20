package com.myproj.webflux.funcation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
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
@Configuration
public class FunctionModelConfig {

    @Bean
    public FunctionPersonHandler handler(){
        return new FunctionPersonHandler();
    }

    /**
     * 用RouterFunctions.route() 创建路由规则的路由构建器 ， 替代了SpringMVC的 @RequestMapping。
     *
     * 注意 : 此行为： 需要至少spring 5.2
     *
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction(final FunctionPersonHandler handler){

        return RouterFunctions.route().GET("/person/getList", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getListV1)
                .GET("/person/get", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getListV2)
                .build();
    }


    public static class FunctionPersonHandler {

        private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        public Mono<ServerResponse> getListV1(ServerRequest serverRequest){
            Flux<String> personList = Flux.just("张三" , "李四" , "王二麻子").publishOn(Schedulers.fromExecutor(EXECUTOR)).map(a -> {
                System.out.println(Thread.currentThread().getName());
                return a;
            });
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(personList,String.class);
        }

        public Mono<ServerResponse> getListV2(ServerRequest serverRequest){
            Mono<String> personList = Mono.just("张三" ).publishOn(Schedulers.fromExecutor(EXECUTOR)).map(a -> {
                System.out.println(Thread.currentThread().getName());
                return a;
            });
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(personList,String.class);
        }
    }


}
