package com.spring.pattern.proxy.dynamicproxy.jdkproxy;

import com.spring.pattern.proxy.dynamicproxy.Person;

/**
 * Created by shanwei on 2019/4/9.
 */
public class Girl implements Person {
    @Override
    public void findLove() {
        System.out.println("高富帅");
        System.out.println("身高180cm");
        System.out.println("有6块腹肌");
    }
}
