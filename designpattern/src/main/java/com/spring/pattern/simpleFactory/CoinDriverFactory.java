package com.spring.pattern.simpleFactory;

import com.spring.pattern.BTCDriver;
import com.spring.pattern.ETHDriver;
import com.spring.pattern.ICoinDriver;

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
