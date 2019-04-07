package com.spring.pattern.factory.simpleFactory;

import com.spring.pattern.factory.BTCDriver;
import com.spring.pattern.factory.ETHDriver;
import com.spring.pattern.factory.ICoinDriver;

/**
 * Created by shanwei on 2019/4/7.
 */
public class CoinDriverFactory {

    public ICoinDriver getCoinDrver(String coinName){
        if ("BTC".equals(coinName)){
            return new BTCDriver();
        }else if ("ETH".equals(coinName)){
            return new ETHDriver();
        }else {
            return null;
        }
    }
}
