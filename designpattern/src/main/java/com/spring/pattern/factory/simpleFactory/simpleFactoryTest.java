package com.spring.pattern.factory.simpleFactory;

import com.spring.pattern.factory.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class simpleFactoryTest {

    public static void main(String[] args) {

        CoinDriverFactory coinDriverFactory = new CoinDriverFactory();
        ICoinDriver coinDriver = coinDriverFactory.getCoinDrver("BTC");
        coinDriver.deposite();

        coinDriver = coinDriverFactory.getCoinDrver("ETH");
        coinDriver.deposite();
    }
}
