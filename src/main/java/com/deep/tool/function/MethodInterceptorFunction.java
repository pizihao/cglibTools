package com.deep.tool.function;

import java.lang.reflect.Method;

/**
 * <h2>{@link net.sf.cglib.proxy.MethodInterceptor}</h2>
 *
 * @author Create by liuwenhao on 2022/5/30 16:11
 */
@FunctionalInterface
public interface MethodInterceptorFunction {

    void exec(Object obj, Method method, Object[] args);

}