package com.spring.pattern.factory.factorymethod;

import com.spring.pattern.factory.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class factoryMethodTest {

    public static void main(String[] args) {

        ICoinDriverFactory coinDriverFactory = new BTCDriverFactory();
        ICoinDriver btcDriver = coinDriverFactory.getCoinDriver();
        btcDriver.deposite();

        coinDriverFactory = new ETHDriverFactory();
        ICoinDriver ethDriver = coinDriverFactory.getCoinDriver();
        ethDriver.deposite();
    }
}
