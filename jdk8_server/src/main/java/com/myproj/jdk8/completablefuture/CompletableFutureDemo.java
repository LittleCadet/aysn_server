package com.myproj.jdk8.completablefuture;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author shenxie
 * @date 2020/9/20
 */
public class CompletableFutureDemo {

    private static ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60 ,
            TimeUnit.SECONDS ,
            new LinkedBlockingQueue<>() ,
            new ThreadFactoryBuilder().setNameFormat("bizThreadPool-%d").build() ,
            new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runAsyn();

//        supplyAsyn();

//        thenRunAsync();

//        thenAcceptAsync();

//        thenApplyAsync();

//        whenCompleteAsync();
    }

    /**
     * runAsyn() , 没有返回值。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void runAsyn() throws ExecutionException, InterruptedException {

        //不指定线程池， 那么实际运行的是FrokJoinPool中的静态线程池： commonPool
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println("第一次运行");
                Thread.sleep(3000);
                calc();
            }
        });

        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));
        System.out.println(String.format("当前线程： %s , runAsyn的执行结果： %s" , Thread.currentThread().getName() , voidCompletableFuture.get()));
    }

    /**
     * supplyAsync（），  有返回值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void supplyAsyn() throws ExecutionException, InterruptedException {

        //指定线程池
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Object>() {

            @SneakyThrows
            @Override
            public Object get() {
                System.out.println("第一次运行");
                Thread.sleep(3000);
                return calc();
            }
        }, executorService);

        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));
        System.out.println(String.format("当前线程： %s , supplyAsyn的执行结果：%s " , Thread.currentThread().getName() , objectCompletableFuture.get()));
    }

    /**
     *
     * 1.thenRunAsync回调事件 没有返回值 ， 且拿不到依赖事件的返回值
     *
     * 2.thenRunAsync的依赖事件和回调事件：
     * 在 都没有指定线程池的情况下，两个事件都会在同一个线程 【ForkJoinPool.commonPool】中执行
     * 在各自有指定线程池【即使线程池相同】的情况下，  两个事件不会再同一个线程中执行。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thenRunAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @SneakyThrows
            @Override
            public Integer get() {
                System.out.println("第一次执行");
                Thread.sleep(3000);
                return calc();
            }
        }, executorService);

        CompletableFuture<Void> 第二次执行 = integerCompletableFuture.thenRunAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二次执行");
                calc();
            }
        } , executorService);

        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));
        System.out.println(String.format("当前线程： %s , thenRunAsync的执行结果： %s" , Thread.currentThread().getName() , 第二次执行.get()));
    }


    /**
     *
     * 1.thenAcceptAsync回调事件 ： 没有返回值 ， 但是可以拿到前者的返回值
     *
     * 2.thenAcceptAsync的依赖事件和回调事件：
     *      * 在 都没有指定线程池的情况下，两个事件都会在同一个线程 【ForkJoinPool.commonPool】中执行
     *      * 在各自有指定线程池的情况下，  两个事件不会再同一个线程中执行。
     *
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thenAcceptAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {

            @SneakyThrows
            @Override
            public Integer get() {
                System.out.println("第一次执行");
                Thread.sleep(3000);
                return calc();
            }
        } , executorService);

        CompletableFuture<Void> 第二次执行 = integerCompletableFuture.thenAcceptAsync(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("第二次执行");
                calc(integer);
            }
        }  , executorService);
        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));
        System.out.println(String.format("当前线程： %s , thenAcceptAsync的执行结果： %s" , Thread.currentThread().getName() , 第二次执行.get()));
    }


    /**
     *
     * 1.thenApplyAsync回调事件 : 有返回值，且能拿到依赖事件的返回值
     *
     * 2.thenApplyAsync的依赖事件 + 回调事件：
     *  * 在 都没有指定线程池的情况下，两个事件都会在同一个线程 【ForkJoinPool.commonPool】中执行
     *  * 在各自有指定线程池【即使线程池相同】的情况下，  两个事件不会再同一个线程中执行。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thenApplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {

            @SneakyThrows
            @Override
            public Integer get() {
                System.out.println("第一次执行");
                Thread.sleep(3000);
                return calc();
            }
        } , executorService);

        CompletableFuture<Integer> 第二次执行 = integerCompletableFuture.thenApplyAsync(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                System.out.println("第二次执行");
                return calc(integer);
            }
        } , executorService);
        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));
        System.out.println(String.format("当前线程： %s , thenApplyAsync的执行结果：%s " , Thread.currentThread().getName() , 第二次执行.get()));
    }


    /**
     * 1.whenComplete  / whenCompleteAsync: 回调函数： 能拿到回调结果，且不会阻塞调用线程
     *
     * 2.whenCompleteAsync : 回调函数：
     *
     *     * 在 都没有指定线程池的情况下，两个事件都会在同一个线程 【ForkJoinPool.commonPool】中执行
     *     * 在各自有指定线程池【即使线程池相同】的情况下，  两个事件不会再同一个线程中执行。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void whenCompleteAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {

            @SneakyThrows
            @Override
            public Integer get() {
                System.out.println("第一次执行");
                Thread.sleep(3000);
                return calc();
            }
        } );

//        integerCompletableFuture.whenComplete(new BiConsumer<Integer, Throwable>() {
//            @Override
//            public void accept(Integer integer, Throwable throwable) {
//                if(null != throwable){
//                    System.out.println(String.format("当前线程： %s , 有异常： %s" , Thread.currentThread().getName() , throwable));
//                }else{
//                    System.out.println(String.format("当前线程： %s , 拿到whenComplete的回调结果 : %s" , Thread.currentThread().getName() , integer));
//                }
//            }
//        } );
        integerCompletableFuture.whenCompleteAsync(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {
                if(null != throwable){
                    System.out.println(String.format("当前线程： %s , 有异常： %s" , Thread.currentThread().getName() , throwable));
                }else{
                    System.out.println(String.format("当前线程： %s , 拿到whenComplete的回调结果 : %s" , Thread.currentThread().getName() , integer));
                }
            }
        } );

        System.out.println(String.format("当前线程： %s " , Thread.currentThread().getName()));

        //等待当前线程的所有的子线程执行完毕。
        Thread.currentThread().join();
    }



    private static int calc(Integer ... args){
        int sum = CollectionUtils.isEmpty(Arrays.asList(args)) ? 0 : args[0];
        for (int i = 0; i <= 100; i++) {
            sum += i;
        }

        System.out.println(String.format("当前线程： %s  , calc的结果：%s" , Thread.currentThread().getName() , sum));
        return sum;
    }

}
