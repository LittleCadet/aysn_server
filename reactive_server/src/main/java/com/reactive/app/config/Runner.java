package com.reactive.app.config;

import com.reactive.app.demo.OrderInfo;
import com.reactive.app.demo.OrderObserver;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


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
