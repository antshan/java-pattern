package com.spring.pattern.singleton.serializable;

import java.io.Serializable;

/**
 * Created by shanwei on 2019/4/8.
 */
public class SerializableSingleton implements Serializable {

    private static final SerializableSingleton INSTANCE = new SerializableSingleton();

    private SerializableSingleton(){}

    public static SerializableSingleton getInstance(){
        return INSTANCE;
    }

    private Object readResolve(){
        return INSTANCE;
    }
}
