package com.reactive.app.rxjava.defer;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shenxie
 * @date 2020/11/19
 */
public class DeferDemo {

    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger();
        Disposable subscribe = Observable.range(1, 10)
                .doOnNext(a -> count.getAndIncrement())
                .ignoreElements()

                // RxJava中defer的延迟计算， 避免在编译的时候运行。 而是等到原始流结束以后才运行
                .andThen(Single.defer(() -> Single.just(count.get())))
                .subscribe(System.out::print);
        System.out.println("=======result: "+ subscribe);
    }
}
