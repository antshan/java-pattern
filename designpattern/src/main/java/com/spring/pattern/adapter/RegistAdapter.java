package com.spring.pattern.adapter;

import com.spring.pattern.adapter.ResultMsg;

/**
 *
 */
public interface RegistAdapter {
    boolean support(Object adapter);
    ResultMsg login(String id, Object adapter);
}
