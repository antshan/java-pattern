package com.spring.pattern;

/**
 * Created by shanwei on 2019/4/7.
 */
public class ETHDriver implements ICoinDriver {
    @Override
    public void deposite() {
        System.out.println("充值ETH");
    }
}
