package com.spring.pattern.simpleFactory;

import com.spring.pattern.ICoinDriver;

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
