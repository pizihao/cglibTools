package com.deep.tool.proxy;

import com.deep.tool.model.User;
import junit.framework.TestCase;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/26 16:29
 */
public class EnhancerToolsTest extends TestCase {

    static List<String> strings = new ArrayList<>();

    static {
        strings.add("hashCode");
        strings.add("equals");
        strings.add("clone");
        strings.add("toString");
        strings.add("finalize");
    }

    public void testCallbackMethodInterceptor() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(User.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> {
            before(method.getName());
            Object o = proxy.invokeSuper(obj, args1);
            after(method.getName());
            return o;
        });
        Class<?>[] classes = new Class[]{Integer.class, String.class, Integer.class};
        Object[] objects = new Object[]{123, "时间", 456};
        User o = (User) enhancer.create(classes, objects);
        assertEquals(o.getName(), "时间");
    }

    public void testCallbackLazyLoader() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(User.class);
        // 在实例化的对象中的被代理的方法调用之前不会被调用，仅限于被代理的对象
        enhancer.setCallback((LazyLoader) () -> {
            before(null);
            User user = new User(111, "保存", 100);
            after(null);
            return user;
        });
        User user = (User) enhancer.create();
        System.out.println(user.getName());
        System.out.println(user.getName());
    }

    public void testCallbackDispatcher() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(User.class);
        // 不可避免的会对Object中的方法进行代理，每次调用被代理方法都会执行回调
        enhancer.setCallback((Dispatcher) () -> {
            before(null);
            User user = new User(111, "保存", 100);
            after(null);
            return user;
        });

        User user = (User) enhancer.create();
        System.out.println(user.getName());
        System.out.println(user);
    }

    public void testCallbackFixedValue() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(User.class);
        // 强制指定返回值，如果不兼容会抛出异常
        enhancer.setCallback((FixedValue) () -> 10001);
        User user = (User) enhancer.create();
        user.setAge(456);
        System.out.println(user.getAge());
    }

    public void testCallbackFilterFixedValue() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(User.class);
        String name = "getAge";
        CallbackHelper callbackHelper = new CallbackHelper(User.class, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                if (name.equals(method.getName())) {
                    return (FixedValue) () -> 123456789;
                }
                return NoOp.INSTANCE;
            }
        };
        enhancer.setCallbackFilter(callbackHelper);
        enhancer.setCallback((Dispatcher) () -> new User(111, "保存", 100));
        enhancer.setCallbacks(callbackHelper.getCallbacks());
        Class<?>[] classes = new Class[]{Integer.class, String.class, Integer.class};
        Object[] objects = new Object[]{123, "时间", 456};
        User user = (User) enhancer.create(classes, objects);
        System.out.println(user.getAge());
        System.out.println(user.toString());
    }

    private static void before(String s) {
        boolean sign = strings.contains(s);
        if (!sign) {
            System.out.println(111);
        }
    }

    private static void after(String s) {
        boolean sign = strings.contains(s);
        if (!sign) {
            System.out.println(666);
        }
    }

}