package com.spring.pattern.decorator;

/**
 * Created by shanwei on 2019/4/12.
 */
public class SausageDecorator extends BattercakeDecorator {

    public SausageDecorator(Battercake battercake) {
        super(battercake);
    }

    public String getMsg() {
        return super.getMsg()+"+1根香肠";
    }

    public int getPrice() {
        return super.getPrice()+3;
    }
}
