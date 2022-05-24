package com.deep.tool.util;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h2>集合工具类</h2>
 *
 * @author Create by liuwenhao on 2022/5/24 17:08
 */
public class CollUtil {
    private CollUtil() {
    }

    /**
     * toList
     *
     * @param e   集合
     * @param fun 映射方法
     * @return java.util.List<T>
     * @author liuwenhao
     * @date 2022/5/24 17:12
     */
    public static <T, E> List<T> list(Collection<? extends E> e, Function<E, ? extends T> fun) {
        return e.stream().map(fun).collect(Collectors.toList());
    }

    /**
     * toSet
     *
     * @param e   集合
     * @param fun 映射方法
     * @return java.util.List<T>
     * @author liuwenhao
     * @date 2022/5/24 17:12
     */
    public static <T, E> Set<T> set(Collection<? extends E> e, Function<E, ? extends T> fun) {
        return e.stream().map(fun).collect(Collectors.toSet());
    }

    /**
     * toArray
     *
     * @param e   集合
     * @param fun 映射方法
     * @return java.util.List<T>
     * @author liuwenhao
     * @date 2022/5/24 17:12
     */
    @SuppressWarnings("unchecked")
    public static <T, E> T[] array(Collection<? extends E> e, Function<E, ? extends T> fun) {
        return (T[]) e.stream().map(fun).toArray();
    }

}

