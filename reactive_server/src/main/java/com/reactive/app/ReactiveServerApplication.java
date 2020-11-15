package com.reactive.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shenxie
 * @date 2020/8/26
 */
@SpringBootApplication
public class ReactiveServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveServerApplication.class, args);
    }

}
