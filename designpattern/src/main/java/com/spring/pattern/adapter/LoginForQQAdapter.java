package com.spring.pattern.adapter;

import com.spring.pattern.adapter.ResultMsg;

/**
 *
 */
public class LoginForQQAdapter implements LoginAdapter {
    public boolean support(Object adapter) {
        return adapter instanceof LoginForQQAdapter;
    }

    public ResultMsg login(String id, Object adapter) {
        return null;
    }
}
