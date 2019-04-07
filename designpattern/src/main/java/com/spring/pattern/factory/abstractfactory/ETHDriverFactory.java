package com.spring.pattern.factory.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class ETHDriverFactory implements ICoinDriverFactory {
    @Override
    public INewAddress createNewAddress() {
        return new ETHNewAddress();
    }

    @Override
    public IBalance getBalance() {
        return new ETHBalance();
    }
}
