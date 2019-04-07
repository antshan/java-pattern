package com.spring.pattern.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCNewAddress implements INewAddress {
    @Override
    public void generateNewAddress() {
        System.out.println("生成BTC地址");
    }
}
