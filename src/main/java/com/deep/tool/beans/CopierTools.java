package com.deep.tool.beans;

import com.deep.tool.annotations.DerivedTypes;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import net.sf.cglib.core.KeyFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/18 4:58
 */
@DerivedTypes
public class CopierTools {

    /**
     * 缓存，使用ConcurrentHashMap来存放，保证线程安全
     */
    static Map<Object, BeanCopier> map = new ConcurrentHashMap<>();

    /**
     * 静态修饰，确保每次用的都是这个generateKey
     */
    static GenerateKey generateKey = (GenerateKey) KeyFactory.create(GenerateKey.class);

    /**
     * <h2>获得一个BeanCopier</h2>
     * 优先从缓存中获取，如果缓存中不存在则创建<br>
     * 默认不使用转换器
     *
     * @param r 源类型
     * @param t 目标类型
     * @return net.sf.cglib.beans.BeanCopier
     * @author liuwenhao
     * @date 2022/4/18 9:09
     */
    public static BeanCopier create(Class<?> r, Class<?> t) {
        return create(r, t, false);
    }

    /**
     * <h2>获得一个BeanCopier</h2>
     * 优先从缓存中获取，如果缓存中不存在则创建
     *
     * @param r            类1，源类型
     * @param t            类2，目标类型
     * @param useConverter 是否使用转换器
     * @return net.sf.cglib.beans.BeanCopier
     * @author liuwenhao
     * @date 2022/4/18 9:09
     */
    public static BeanCopier create(Class<?> r, Class<?> t, boolean useConverter) {
        Object key = generateKey.create(r, t, useConverter);
        return map.computeIfAbsent(key, o -> BeanCopier.create(r, t, useConverter));
    }

    /**
     * <h2>属性copy</h2>
     * 将r(源)中的值覆盖的装入到t(目标)中<br>
     * 不使用转换器
     *
     * @param r 源对象
     * @param t 目标对象
     * @author Created by liuwenhao on 2022/5/21 17:47
     */
    public static void copy(Object r, Object t) {
        BeanCopier beanCopier = create(r.getClass(), t.getClass());
        beanCopier.copy(r, t, null);
    }

    /**
     * <h2>属性copy</h2>
     * 将r(源)中的值覆盖的装入到t(目标)中<br>
     *
     * @param <T>    目标类型
     * @param r      源对象
     * @param tClass 目标类实例
     * @return T
     * @author Created by liuwenhao on 2022/5/21 18:04
     */
    public static <T> T newInstanceCopy(Object r, Class<T> tClass) {
        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
        T tInstance = constructorAccess.newInstance();
        copy(r, tInstance);
        return tInstance;
    }

    /**
     * <h2>属性copy</h2>
     * 将r(源)中的值覆盖的装入到t(目标)中<br>
     * 通过泛型来确定源和目标的具体类型
     *
     * @param <R> 源对象类型
     * @param <T> 目标对象类型
     * @param r   源对象
     * @param t   目标对象
     * @return T
     * @author Created by liuwenhao on 2022/5/21 17:53
     */
    public static <R, T> T copyAcquire(R r, T t) {
        copy(r, t);
        return t;
    }

    /**
     * <h2>属性copy</h2>
     * Collection中每一个元素都转化为目标对象<br>
     * 使用泛型固定其真实类型
     *
     * @param <R>         源对象类型
     * @param <T>         目标对象类型
     * @param rCollection 源对象集合
     * @param tClass      目标类对象
     * @return java.util.List<T>
     * @author Created by liuwenhao on 2022/5/21 21:07
     */
    public static <R, T> Collection<T> copyCollectionAcquire(Collection<R> rCollection, Class<T> tClass) {
        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
        return rCollection.stream().map(r -> {
            T tInstance = constructorAccess.newInstance();
            copy(r, tInstance);
            return tInstance;
        }).collect(Collectors.toList());
    }

    /**
     * <h2>属性copy</h2>
     *
     * @param <R>       源对象类型
     * @param <T>       目标对象类型
     * @param rIterator 源对象集合
     * @param tClass    目标类对象
     * @return java.util.Iterator<T>
     * @author Created by liuwenhao on 2022/5/21 21:23
     */
    public static <R, T> Iterator<T> copyIteratorAcquire(Iterator<R> rIterator, Class<T> tClass) {
        return null;
    }

    interface GenerateKey {
        /**
         * <h2>生成key</h2>
         *
         * @param c1           类1
         * @param c2           类2
         * @param useConverter 是否使用转换器
         * @return java.lang.Object
         * @author liuwenhao
         * @date 2022/4/18 8:58
         */
        Object create(Class<?> c1, Class<?> c2, boolean useConverter);
    }

}


