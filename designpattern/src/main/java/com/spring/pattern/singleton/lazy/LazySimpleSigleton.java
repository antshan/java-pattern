
package com.spring.pattern.singleton.lazy;

/**
 * Created by shanwei on 2019/4/8.
 */
public class LazySimpleSigleton {

    private static LazySimpleSigleton lazySimpleSigleton= null;

    private LazySimpleSigleton(){}

    public static synchronized LazySimpleSigleton getInstance(){

        if (lazySimpleSigleton==null){
            lazySimpleSigleton= new LazySimpleSigleton();
        }

        return lazySimpleSigleton;
    }
}
