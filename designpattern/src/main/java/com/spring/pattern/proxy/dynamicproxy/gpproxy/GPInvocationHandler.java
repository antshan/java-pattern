package com.spring.pattern.proxy.dynamicproxy.gpproxy;

import java.lang.reflect.Method;

/**
 * Created by shanwei on 2019/4/9.
 */
public interface GPInvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
}
