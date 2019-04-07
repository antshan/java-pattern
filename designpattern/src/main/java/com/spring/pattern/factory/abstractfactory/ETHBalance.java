package com.spring.pattern.factory.abstractfactory;

/**
 * Created by shanwei on 2019/4/7.
 */
public class ETHBalance implements IBalance {
    @Override
    public void computeBalance() {
        System.out.println("获取ETH保有量");
    }
}
