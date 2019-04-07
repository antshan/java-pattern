package com.spring.pattern.factorymethod;

import com.spring.pattern.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public interface ICoinDriverFactory {

    ICoinDriver getCoinDriver();
}
