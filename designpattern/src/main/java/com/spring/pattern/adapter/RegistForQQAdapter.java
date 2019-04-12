package com.spring.pattern.adapter;

import com.spring.pattern.adapter.ResultMsg;

/**
 *
 */
public class RegistForQQAdapter implements RegistAdapter,LoginAdapter {
    public boolean support(Object adapter) {
        return false;
    }

    public ResultMsg login(String id, Object adapter) {
        return null;
    }
}
