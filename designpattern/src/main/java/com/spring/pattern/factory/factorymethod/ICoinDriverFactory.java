package com.spring.pattern.factory.factorymethod;

import com.spring.pattern.factory.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public interface ICoinDriverFactory {

    ICoinDriver getCoinDriver();
}
