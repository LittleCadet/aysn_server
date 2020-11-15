package com.reactive.app.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenxie
 * @date 2020/8/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private String orderNo;

    private String detail;
}
