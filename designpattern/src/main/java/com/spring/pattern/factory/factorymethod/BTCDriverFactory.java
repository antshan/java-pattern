package com.spring.pattern.factory.factorymethod;

import com.spring.pattern.factory.BTCDriver;
import com.spring.pattern.factory.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCDriverFactory implements ICoinDriverFactory {
    @Override
    public ICoinDriver getCoinDriver() {
        return new BTCDriver();
    }
}
