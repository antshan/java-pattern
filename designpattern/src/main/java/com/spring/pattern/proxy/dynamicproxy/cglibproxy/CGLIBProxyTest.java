package com.spring.pattern.proxy.dynamicproxy.cglibproxy;

/**
 * Created by shanwei on 2019/4/9.
 */
public class CGLIBProxyTest {

    public static void main(String[] args) {

        Customer obj = (Customer) new CGLIBMeipo().getInstance(Customer.class);
        obj.findLove();
    }
}
