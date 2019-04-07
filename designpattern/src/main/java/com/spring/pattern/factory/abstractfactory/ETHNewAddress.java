package com.spring.pattern.factory.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class ETHNewAddress implements INewAddress {
    @Override
    public void generateNewAddress() {
        System.out.println("生成ETH钱包地址");
    }
}
