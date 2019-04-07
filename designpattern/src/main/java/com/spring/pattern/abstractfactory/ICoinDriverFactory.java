package com.spring.pattern.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public interface ICoinDriverFactory {

    INewAddress createNewAddress();

    IBalance getBalance();
}
