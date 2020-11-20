package com.reactive.app.rxjava.config;

import com.reactive.app.rxjava.demo.OrderInfo;
import com.reactive.app.rxjava.demo.OrderObserver;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;


/**
 * @author shenxie
 * @date 2020/11/15
 */
@Slf4j
@Configuration
public class Runner implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        OrderInfo orderInfo = new OrderInfo("12345678" , "这是一个订单");
        Observable.just(orderInfo)
                .observeOn(Schedulers.io())
                .subscribe(new OrderObserver());
    }
}
