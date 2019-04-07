package com.spring.pattern.factory.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public interface ICoinDriverFactory {

    INewAddress createNewAddress();

    IBalance getBalance();
}
