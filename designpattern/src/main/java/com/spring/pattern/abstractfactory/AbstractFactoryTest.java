package com.spring.pattern.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class AbstractFactoryTest {

    public static void main(String[] args) {

        ICoinDriverFactory coinDriverFactory = new BTCDriverFactory();
        coinDriverFactory.createNewAddress().generateNewAddress();
        coinDriverFactory.getBalance().computeBalance();

        coinDriverFactory = new ETHDriverFactory();
        coinDriverFactory.createNewAddress().generateNewAddress();
        coinDriverFactory.getBalance().computeBalance();
    }
}
