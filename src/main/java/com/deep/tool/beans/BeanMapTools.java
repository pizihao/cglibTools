package com.deep.tool.beans;

import com.deep.tool.annotations.EdgeTypes;
import com.deep.tool.annotations.ExtensionType;
import com.deep.tool.exception.TypeMismatchException;
import com.deep.tool.jackson.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

/**
 * <h2>BeanMap</h2>
 * <table>
 *        <th>
 *            <td>方法名</td>
 *            <td>:</td>
 *            <td>说明</td>
 *        </th>
 *        <tr>
 *            <td>{@link #create()}</td>
 *            <td>:</td>
 *            <td>创建BeanMap，内容为空</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #create(Object)} </td>
 *            <td>:</td>
 *            <td>通过bean创建BeanMap</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #create(BeanMap, Object)} </td>
 *            <td>:</td>
 *            <td>将一个BeanMap重新指定bean，从而形成新的BeanMap</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #replace(BeanMap, Object)} </td>
 *            <td>:</td>
 *            <td>替换BeanMap中的bean，替换前后BeanMap不变</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #beanToMap(Object)} </td>
 *            <td>:</td>
 *            <td>bean转化为map</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #beanMapToBean(BeanMap, Class)}  </td>
 *            <td>:</td>
 *            <td>beanMap转化为bean</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #getBean(BeanMap)}  </td>
 *            <td>:</td>
 *            <td>beanMap转化为bean</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #isEmpty(BeanMap)}  </td>
 *            <td>:</td>
 *            <td>判断beanMap是否为空，beanMap为null或无元素会返回true，反之为false</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #isNotEmpty(BeanMap)} </td>
 *            <td>:</td>
 *            <td>判断beanMap是否不为空，beanMap为null或无元素会返回false，反之为true</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #set(BeanMap, String, Object)}  </td>
 *            <td>:</td>
 *            <td>将一组键值对加入BeanMap，如果键已存在则抛弃</td>
 *        </tr>
 *        <tr>
 *            <td>{@link #put(BeanMap, String, Object)}  </td>
 *            <td>:</td>
 *            <td>将一组键值对加入BeanMap，如果键已存在则覆盖</td>
 *        </tr>
 * </table>
 *
 * @author Create by liuwenhao on 2022/5/24 18:16
 */
@EdgeTypes
@ExtensionType
@SuppressWarnings("unused")
public class BeanMapTools {

    private BeanMapTools() {
    }

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
    public static Map<String, Object> beanToMap(Object o) {
        return create(o);
    }

    /**
     * <h2>beanMap转化为bean</h2>
     * 不限制tClass的类型，除非是当前beanMap无法转换的
     *
     * @param beanMap BeanMap对象
     * @param tClass  需要转化的类型
     * @return T
     * @author liuwenhao
     * @date 2022/5/26 10:12
     */
    public static <T> T beanMapToBean(BeanMap beanMap, Class<T> tClass) {
        ObjectMapper objectMapper = ObjectMapperFactory.get();
        try {
            return objectMapper.readValue(beanMap.toString(), tClass);
        } catch (Exception e) {
            throw TypeMismatchException.exception("无法转换的类型 --> {}", tClass.getName());
        }
    }

    /**
     * <h2>beanMap转化为bean</h2>
     * 限制bean的类型，仅为beanMap转化前的类型
     *
     * @param beanMap BeanMap对象
     * @return T
     * @author liuwenhao
     * @date 2022/5/26 10:26
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(BeanMap beanMap) {
        return (T) beanMap.getBean();
    }

    /**
     * <h2>判断beanMap是否为空</h2>
     * beanMap为null或无元素会返回true，反之为false
     *
     * @param beanMap BeanMap对象
     * @return boolean
     * @author liuwenhao
     * @date 2022/5/26 10:40
     */
    public static boolean isEmpty(BeanMap beanMap) {
        return beanMap == null || beanMap.isEmpty();
    }

    /**
     * <h2>判断beanMap是否不为空</h2>
     * beanMap为null或无元素会返回false，反之为true
     *
     * @param beanMap BeanMap对象
     * @return boolean
     * @author liuwenhao
     * @date 2022/5/26 10:40
     */
    public static boolean isNotEmpty(BeanMap beanMap) {
        return beanMap != null && !beanMap.isEmpty();
    }

    /**
     * <h2>将一组键值对加入BeanMap</h2>
     * 如果键已存在则抛弃
     *
     * @param beanMap BeanMap对象
     * @param key     键
     * @param value   值
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/26 10:50
     */
    @SuppressWarnings("unchecked")
    public static BeanMap set(BeanMap beanMap, String key, Object value) {
        beanMap.computeIfAbsent(key, o -> value);
        return beanMap;
    }

    /**
     * <h2>将一组键值对加入BeanMap</h2>
     * 如果键已存在则覆盖
     *
     * @param beanMap BeanMap对象
     * @param key     键
     * @param value   值
     * @return net.sf.cglib.beans.BeanMap
     * @author liuwenhao
     * @date 2022/5/26 10:50
     */
    public static BeanMap put(BeanMap beanMap, String key, Object value) {
        beanMap.put(key, value);
        return beanMap;
    }

}