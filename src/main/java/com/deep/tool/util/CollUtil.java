package com.deep.tool.util;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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


    /**
     * 验证两个集合相同索引的数据是否相等
     *
     * @param tList1 集合1
     * @param tList2 集合2
     * @return boolean
     * @author liuwenhao
     * @date 2022/5/24 17:17
     */
    public static <T> boolean equal(List<T> tList1, List<T> tList2) {
        if (tList1.size() != tList2.size()) {
            return false;
        }
        for (int i = 0; i < tList1.size(); i++) {
            T t1 = tList1.get(i);
            T t2 = tList2.get(i);
            if (!t1.equals(t2)) {
                return false;
            }
        }
        return true;
    }
}