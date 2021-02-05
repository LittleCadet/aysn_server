package com.reactive.app.reactor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author shenxie
 * @date 2021/2/4
 */
public class FluxDemo {

    public static void main(String[] args) {
//        onTest();
//        doOnSubscribe();
//        createFlux();
        delaySubscription();
//        test();
    }

    /**
     * subscribeOn:
     * a.不受定义位置的影响，从源头开始生效
     * b.从源头开始改变执行的线程【新建一个线程去处理IO】
     * publishOn:
     * a.受到定义位置的影响， 从定义位置，往后开始生效
     * b.改变当前的执行线程【新建一个线程去处理IO】
     * subscribe:
     * 不订阅，那么flux的代码不执行
     */
    public static void onTest() {
        Flux.just("tom")
                .map(t -> {
                    System.out.println("[map]" + Thread.currentThread().getName());
                    return t.concat("@163.com");
                })
                .publishOn(Schedulers.newElastic("THREAD_PUBLISH_ON"))
                .filter(t -> {
                    System.out.println("[filter]" + Thread.currentThread().getName());
                    return t.startsWith("t");
                })
                .subscribeOn(Schedulers.newElastic("THREAD_SUBSCRIBE_ON"))
                .subscribe(t -> {
                    System.out.println("[subscribe]" + Thread.currentThread().getName());
                })
        ;
    }

    /**
     * 1.多个subscribeOn: 后面的操作符使用的线程： 会以第一个subscribeOn定义的为准
     * 2.doOnSubscribe：与start()不同的是，可以指定线程： 使用当前doOnSubscribe后面的第一个subscribeOn的定义的线程为准。
     * 3.多个doOnSubscribe: 按照从下往上的顺序执行
     */
    public static void doOnSubscribe() {
        Flux.just("tom")
                .doOnSubscribe(t -> {
                    System.out.println("00:" + Thread.currentThread().getName());
                })
                .subscribeOn(Schedulers.newElastic("THREAD_0"))
                .map(t -> {
                    System.out.println("[map0]:" + Thread.currentThread().getName());
                    return t;
                })
                .doOnSubscribe(t -> {
                    System.out.println("11:" + Thread.currentThread().getName());
                })
                .subscribeOn(Schedulers.newElastic("THREAD_1"))
                .map(t -> {
                    System.out.println("[map1]:" + Thread.currentThread().getName());
                    return t;
                })
                .doOnSubscribe(t -> {
                    System.out.println("22:" + Thread.currentThread().getName());
                })
                .subscribeOn(Schedulers.newElastic("THREAD_2"))
                .subscribe(t -> {
                    System.out.println("[subscribe]" + Thread.currentThread().getName());
                });
    }

    /**
     * 创建flux的常用方式：
     */
    @SneakyThrows
    public static void createFlux() {
        // 以编程的方式创建一个flux实例 ： 具备多次发射能力
        Flux.create(t -> {
            t.next(1);
            t.next(2);
            t.next(3);
        })
                .subscribe(t -> {
                    System.out.println("create:" + t);
                });

        // 从流中创建flux实例 ： 具备多次发射能力
        Flux.fromStream(Lists.newArrayList(1, 2, 3).stream())
                .subscribe(t -> {
                    System.out.println("fromStream:" + t);
                });

        // 延迟提供publisher ： 具备多次发射能力
        Flux.defer(() -> {
            return Flux.fromArray(new Integer[]{1, 2, 3});
        })
                .subscribe(t -> {
                    System.out.println("defer:" + t);
                });

        // 创建一个flux： 从0开始递增：永不停止
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(t -> {
                    System.out.println("interval:" + t);
                });

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @SneakyThrows
    public static void delaySubscription() {
        Flux.fromIterable(Lists.newArrayList(1, 2, 3))
                // 延迟订阅
                .delaySubscription(Duration.ofSeconds(1))
                // 意在监控：flux被订阅了
                .doOnSubscribe(t -> {
                    System.out.println("本次运行开始：" + Thread.currentThread().getName());
                })
                // 意在监控： flux处理完成
                .doOnComplete(() -> {
                    System.out.println("完成");
                })
                // 该flux每发送一个元素，就会立即触发一次【但是晚于doOnSubscribe】
//                .doOnNext(t -> next())
                .doOnNext(t -> {
                    System.out.println("doOnNext()");
                })
                // 貌似没啥用： 不是用于捕获异常的
                .doOnError(t -> {
                    System.out.println("失败" + t);
                })
                .onErrorResume(t -> {
                    System.out.println("onErrorResume:" + t);
                    return Flux.error(new Exception("onErrorResume抛出异常"));
                })
                // 意在监控： flux：取消文星
                // 如果有发射的元素 处理失败，那么其余元素会自动取消运行
                .doOnCancel(() -> {
                    System.out.println("取消运行");
                })
                .flatMap(t -> {
                    HashSet<Integer> set = Sets.newHashSet();
                    if (1 == t) {
                        set = Sets.newHashSet(2, 3, 4);
//                        return Flux.error(new Exception("flux.error"));

                    }
                    System.out.println("处理数据:" + t);
//                    return Flux.fromIterable(set)
//                            .doOnSubscribe(t1 -> {
//                                System.out.println("嵌套子订阅开始运行:");
//                            })
//                            .doOnComplete(() -> {
//                                System.out.println("嵌套子任务完成");
//                            });
                    return next(t);
                })
                // 不论是否报错：都会执行，类似于finally
                // doFinally 比 doOnComplete早执行
                .doFinally(t -> {
                    System.out.println("doFinally执行了");
                })
//                .subscribe(t -> {
//                    System.out.println("订阅:" + t);
//                })
                .subscribe();
        // 取消运行
//                .dispose()
        ;

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    public static Publisher<Void> next(int num){
        return Mono.just(num ++)
                .subscribeOn(Schedulers.single())
                .doOnNext(t -> next())
                .doOnSubscribe(t -> {
                    System.out.println("子Mono开始运行:" + t);
                })
                .doOnSuccess(t -> {
                    System.out.println("子mono处理成功:" + t);
                })
                .then();
    }

    public static void next(){
        System.out.println("直接next()");
    }

    /**
     * map需要放在flatMap前面，不然map不运行。
     * 运行顺序： map => flatMap
     */
    @SneakyThrows
    public static void test(){
        Flux.just(1)
                .map(t -> {
                    System.out.println("map");
                    return t;
                })
                .flatMap(FluxDemo::callbackAll)
                .doOnSubscribe(t -> {
                    System.out.println("订阅：" + t);
                })
                .subscribe();

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    public static Publisher<Void> callbackAll(Integer num) {
        System.out.println("flatMap");
        Mono<Integer> mono = Mono.just(num)
                .doOnSubscribe(t -> {
                    System.out.println("callbackAll:" + t);
                })
                .doOnNext(result -> {
                    System.out.println("=====================invokeCallbackAll================");
                })
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    System.out.println("处理错误");
                    return Mono.empty();
                });
//        mono.subscribe();
        return mono.then();
    }
}
