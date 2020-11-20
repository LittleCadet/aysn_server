package com.reactive.app.rxjavaExchangeReactor;

import io.reactivex.Flowable;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

/**
 * @author shenxie
 * @date 2020/11/19
 */
public class Exchange {

    /**
     * 将RxJava的Flowable ==>  Flux，  即可完成从RxJava ==> Reactor的转换。
     * @param args
     */
    public static void main(String[] args) {
        Disposable subscribe = Flux.fromArray(new String[]{"1", "2"})
                .filter(a -> a.equals("1"))
                .subscribe(System.out::print);
    }
}
