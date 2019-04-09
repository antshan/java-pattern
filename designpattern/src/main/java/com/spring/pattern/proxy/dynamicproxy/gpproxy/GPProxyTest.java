package com.spring.pattern.proxy.dynamicproxy.gpproxy;

import com.spring.pattern.proxy.dynamicproxy.Person;
import com.spring.pattern.proxy.dynamicproxy.jdkproxy.Girl;

/**
 * Created by shanwei on 2019/4/9.
 */
public class GPProxyTest {

    public static void main(String[] args) {
        try {

            //JDK动态代理的实现原理
            Person obj = (Person) new GPMeipo().getInstance(new Girl());
            System.out.println(obj.getClass());
            obj.findLove();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
