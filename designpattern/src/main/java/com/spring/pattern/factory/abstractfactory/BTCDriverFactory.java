package com.spring.pattern.factory.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCDriverFactory implements ICoinDriverFactory {
    @Override
    public INewAddress createNewAddress() {
        return new BTCNewAddress();
    }

    @Override
    public IBalance getBalance() {
        return new BTCBalance();
    }
}
