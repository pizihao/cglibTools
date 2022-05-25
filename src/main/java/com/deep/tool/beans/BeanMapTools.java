package com.deep.tool.beans;

import com.deep.tool.annotations.EdgeTypes;
import com.deep.tool.annotations.ExtensionType;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

/**
 * <h2>BeanMap</h2>
 *
 * @author Create by liuwenhao on 2022/5/24 18:16
 */
@EdgeTypes
@ExtensionType
public class BeanMapTools {

    static final Object OBJECT_NULL = null;

    /**
     * <h2>创建BeanMap</h2>
     *
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/25 17:31
     */
    public static BeanMap create() {
        return BeanMap.create(OBJECT_NULL);
    }

    /**
     * <h2>通过bean创建BeanMap</h2>
     *
     * @param o bean
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/25 17:26
     */
    public static BeanMap create(Object o) {
        return BeanMap.create(o);
    }

    /**
     * <h2>创建BeanMap</h2>
     * 将一个BeanMap重新指定bean，从而形成新的BeanMap
     *
     * @param beanMap BeanMap对象
     * @param o       替换后的对象
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/25 17:32
     */
    public static BeanMap create(BeanMap beanMap, Object o) {
        return beanMap.newInstance(o);
    }

    /**
     * <h2>替换bean</h2>
     * 替换BeanMap中的bean<br>
     * 注意修改前后的BeanMap实例不变
     *
     * @param beanMap BeanMap对象
     * @param o       替换后的对象
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/25 17:32
     */
    public static BeanMap replace(BeanMap beanMap, Object o) {
        beanMap.setBean(o);
        return beanMap;
    }

    /**
     * <h2>bean转化为map</h2>
     * key 为 String，value 为 Object
     *
     * @param o bean
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author liuwenhao
     * @date 2022/5/25 18:08
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToObjectMap(Object o) {
        return create(o);
    }



}