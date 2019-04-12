package com.spring.pattern.decorator;

/**
 * Created by shanwei on 2019/4/12.
 */
public class BattercakeDecorator extends Battercake {

    private Battercake battercake;

    public BattercakeDecorator(Battercake battercake){
        this.battercake = battercake;
    }

    @Override
    public String getMsg() {
        return this.battercake.getMsg();
    }

    @Override
    public int getPrice() {
        return this.battercake.getPrice();
    }
}
