package com.spring.pattern.factorymethod;

import com.spring.pattern.BTCDriver;
import com.spring.pattern.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCDriverFactory implements ICoinDriverFactory {
    @Override
    public ICoinDriver getCoinDriver() {
        return new BTCDriver();
    }
}
