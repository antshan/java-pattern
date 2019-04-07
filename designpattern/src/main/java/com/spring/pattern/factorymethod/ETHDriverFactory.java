package com.spring.pattern.factorymethod;

import com.spring.pattern.ETHDriver;
import com.spring.pattern.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class ETHDriverFactory implements ICoinDriverFactory {
    @Override
    public ICoinDriver getCoinDriver() {
        return new ETHDriver();
    }
}
