package com.spring.pattern.decorator;

/**
 * Created by shanwei on 2019/4/12.
 */
public class EggDecorator extends BattercakeDecorator {

    public EggDecorator(Battercake battercake) {
        super(battercake);
    }

    public String getMsg() {
        return super.getMsg()+"+1个鸡蛋";
    }

    public int getPrice() {
        return super.getPrice()+2;
    }
}
