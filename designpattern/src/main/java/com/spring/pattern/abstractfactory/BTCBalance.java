package com.spring.pattern.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class BTCBalance implements IBalance {
    @Override
    public void computeBalance() {
        System.out.println("获取BTC钱包保有量");
    }
}
