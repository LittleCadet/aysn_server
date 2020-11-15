package com.myproj.webflux.annotation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenxie
 * @date 2020/11/15
 */
//@RequestMapping("webFlux")
//@RestController
//public class PersonHandler {
//
//    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
//
//    /**
//     * http-nio-8001-exec-4 : 未解放 NETTY的IO线程
//     * @return
//     */
//    @PostMapping("/getList/v1")
//    Flux<String> getListV1(){
//        return Flux.just("张三" , "李四" , "王二麻子").map(a -> {
//            System.out.println(Thread.currentThread().getName());
//            return a;
//        });
//    }
//
//    /**
//     * 及时 解放了NETTY的IO线程
//     * elastic-2
//     * elastic-2
//     * elastic-2
//     * @return
//     */
//    @PostMapping("/getList/v2")
//    Flux<String> getListV2(){
//        return Flux.just("张三" , "李四" , "王二麻子").publishOn(Schedulers.elastic()).map(a -> {
//            System.out.println(Thread.currentThread().getName());
//            return a;
//        });
//    }
//
//    /**
//     * 可使用全局线程池， 避免业务线程不够用
//     * pool-1-thread-1
//     * pool-1-thread-2
//     * pool-1-thread-3
//     * @return
//     */
//    @PostMapping("/getList/v3")
//    Flux<String> getListV3(){
//        return Flux.just("张三" , "李四" , "王二麻子").publishOn(Schedulers.fromExecutor(EXECUTOR)).map(a -> {
//            System.out.println(Thread.currentThread().getName());
//            return a;
//        });
//    }
//}
