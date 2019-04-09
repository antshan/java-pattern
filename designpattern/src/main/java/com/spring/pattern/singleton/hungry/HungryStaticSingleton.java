package com.spring.pattern.singleton.hungry;

/**
 * Created by shanwei on 2019/4/8.
 */
public class HungryStaticSingleton {

    private static HungryStaticSingleton hungryStaticSingleton;

    static {
        hungryStaticSingleton = new HungryStaticSingleton();
    }

    private HungryStaticSingleton(){}

    public static HungryStaticSingleton getInstance(){
        return hungryStaticSingleton;
    }
}
