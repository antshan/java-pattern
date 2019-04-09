package com.spring.pattern.singleton.lazy;

/**
 * Created by shanwei on 2019/4/8.
 */
public class LazyInnerClassSingleton {

    private LazyInnerClassSingleton(){
        if (LazyHolder.Lazy != null){
            throw new RuntimeException("不允许创建多个该类实例");
        }
    }

    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.Lazy;
    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleton Lazy = new LazyInnerClassSingleton();
    }
}
