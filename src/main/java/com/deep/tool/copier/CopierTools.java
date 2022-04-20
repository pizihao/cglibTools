package com.deep.tool.copier;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.KeyFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/4/18 4:58
 */
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


