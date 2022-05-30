package com.deep.tool.proxy;

import com.deep.tool.annotations.DerivedTypes;
import com.deep.tool.function.MethodInterceptorFunction;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Enhancer</h2>
 * 鉴于CGLIB不再维护，所以此处不做太多的扩展，仅提供基础工具
 *
 * @author Create by liuwenhao on 2022/5/26 16:28
 */
@DerivedTypes
public class EnhancerTools {

    /**
     * 拒绝的组。对于组中的数据不进行代理
     */
    static List<String> refuseGroup = new ArrayList<>();

    static {
        refuseGroup.add("hashCode");
        refuseGroup.add("equals");
        refuseGroup.add("clone");
        refuseGroup.add("toString");
        refuseGroup.add("finalize");
    }

    /**
     * <h2>为方法添加前置操作</h2>
     * 单个方法并生成对象
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param fn         添加的前置操作
     * @return net.sf.cglib.proxy.Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:41
     */
    @SuppressWarnings("unchecked")
    public static <T> T before(Class<T> cls, String methodName,
                               MethodInterceptorFunction fn) {
        List<String> list = new ArrayList<>();
        list.add(methodName);
        Enhancer enhancer = beforeEnhancer(cls, list, fn);
        return (T) enhancer.create();
    }

    /**
     * <h2>为方法添加后置操作</h2>
     * 单个方法并生成对象
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param fn         添加的后置操作
     * @return net.sf.cglib.proxy.Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:41
     */
    @SuppressWarnings("unchecked")
    public static <T> T after(Class<T> cls, String methodName,
                              MethodInterceptorFunction fn) {
        List<String> list = new ArrayList<>();
        list.add(methodName);
        Enhancer enhancer = afterEnhancer(cls, list, fn);
        return (T) enhancer.create();
    }

    /**
     * <h2>为方法添加前置和后置操作</h2>
     * 单个方法并生成对象
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param beforeFn   添加的前置操作
     * @param afterFn    添加的后置操作
     * @return net.sf.cglib.proxy.Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:41
     */
    @SuppressWarnings("unchecked")
    public static <T> T after(Class<T> cls, String methodName,
                              MethodInterceptorFunction beforeFn,
                              MethodInterceptorFunction afterFn) {
        List<String> list = new ArrayList<>();
        list.add(methodName);
        Enhancer enhancer = aroundEnhancer(cls, list, beforeFn, afterFn);
        return (T) enhancer.create();
    }

    /**
     * <h2>为方法添加前置操作</h2>
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param fn         添加的前置操作
     * @return Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:20
     */
    public static Enhancer beforeEnhancer(Class<?> cls, List<String> methodName,
                                          MethodInterceptorFunction fn) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallbackFilter(new CallbackHelper(cls, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                if (methodName.contains(method.getName())) {
                    return (MethodInterceptor) (obj, method1, args, proxy) -> {
                        fn.exec(obj, method1, args);
                        return proxy.invokeSuper(obj, args);
                    };
                }
                return NoOp.INSTANCE;
            }
        });
        return enhancer;
    }

    /**
     * <h2>为方法添加后置操作</h2>
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param fn         添加的后置操作
     * @return Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:20
     */
    public static Enhancer afterEnhancer(Class<?> cls, List<String> methodName,
                                         MethodInterceptorFunction fn) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallbackFilter(new CallbackHelper(cls, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                if (methodName.contains(method.getName())) {
                    return (MethodInterceptor) (obj, method1, args, proxy) -> {
                        Object o = proxy.invokeSuper(obj, args);
                        fn.exec(obj, method1, args);
                        return o;
                    };
                }
                return NoOp.INSTANCE;
            }
        });
        return enhancer;
    }

    /**
     * <h2>为方法添加前置和后置操作</h2>
     *
     * @param cls        类对象
     * @param methodName 需要被代理的方法名
     * @param beforeFn   添加的前置操作
     * @param afterFn    添加的后置操作
     * @return Enhancer
     * @author liuwenhao
     * @date 2022/5/30 16:20
     */
    public static Enhancer aroundEnhancer(Class<?> cls, List<String> methodName,
                                          MethodInterceptorFunction beforeFn,
                                          MethodInterceptorFunction afterFn) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallbackFilter(new CallbackHelper(cls, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                if (methodName.contains(method.getName())) {
                    return (MethodInterceptor) (obj, method1, args, proxy) -> {
                        beforeFn.exec(obj, method1, args);
                        Object o = proxy.invokeSuper(obj, args);
                        afterFn.exec(obj, method1, args);
                        return o;
                    };
                }
                return NoOp.INSTANCE;
            }
        });
        return enhancer;
    }


}