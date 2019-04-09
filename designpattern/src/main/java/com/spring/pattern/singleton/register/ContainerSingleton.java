package com.spring.pattern.singleton.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shanwei on 2019/4/8.
 */
public class ContainerSingleton {

    private ContainerSingleton(){}
    private static Map<String,Object> ioc = new ConcurrentHashMap<>();

    public static Object getBean(String className){
        synchronized (ioc){
            if (!ioc.containsKey(className)){
                Object obj = null;
                try {
                    obj = Class.forName(className).newInstance();
                    ioc.put(className,obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return obj;
            }else {
                return ioc.get(className);
            }
        }
    }
}