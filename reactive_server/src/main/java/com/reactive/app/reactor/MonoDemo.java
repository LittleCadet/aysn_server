package com.reactive.app.reactor;

import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author shenxie
 * @date 2021/2/5
 */
public class MonoDemo {

    public static void main(String[] args) {
        doOnSuccess();

    }

    @SneakyThrows
    public static Publisher<Void> doOnSuccess(){

        Mono.just(1)
                .doOnSubscribe(t -> {
                    System.out.println("订阅了：" + t);
                })
                .flatMap(t -> {
                    System.out.println("数据操作");
                    Mono<Publisher<Integer>> map = Mono.just(2)
                            .map(MonoDemo::next);
                    return map;
                })
                .doOnSuccess(t -> {
                    System.out.println("处理成功:" + t);
                })
                .doOnNext(action -> {
                    System.out.println("下一步处理开始");
                })
                .delaySubscription(Duration.ofSeconds(1))
                .subscribe()
//                .then()
        ;

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
        return Mono.empty();
    }

    public static Publisher<Integer> next(int num){
        Mono<Integer> integerMono = Mono.just(num++)
                .subscribeOn(Schedulers.single())
                .doOnSubscribe(t -> {
                    System.out.println("子处理开始：" + t);
                })
                .doOnNext(t -> sum())
                .doOnSuccess(t -> {
                    System.out.println("子处理成功:" + t);
                });
//        integerMono.subscribe();
        return integerMono;
    }

    public static void sum(){
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        System.out.println("sum:" + sum);
    }
}
