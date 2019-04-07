package com.spring.pattern;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCDriver implements ICoinDriver {
    @Override
    public void deposite() {
        System.out.println("充值BTC");
    }
}
