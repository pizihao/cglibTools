package com.deep.tool.beans;

import com.deep.tool.annotations.DerivedTypes;
import com.deep.tool.exception.MethodNotFondException;
import com.deep.tool.exception.TypeMismatchException;
import com.deep.tool.exception.UnequalLengthException;
import com.deep.tool.util.CollUtil;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.core.KeyFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <h2>Bulk</h2>
 * <table>
 *       <th>
 *           <td>方法名</td>
 *           <td>:</td>
 *           <td>说明</td>
 *       </th>
 *       <tr>
 *           <td>{@link #create(Class, String[], String[], Class[])}</td>
 *           <td>:</td>
 *           <td>构建BulkBean，使用数组区分属性</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #create(Class, Set, Set, List)}</td>
 *           <td>:</td>
 *           <td>构建BulkBean，使用集合区分属性</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #create(Class, boolean, String...)}</td>
 *           <td>:</td>
 *           <td>构建BulkBean，并提供忽略或指定的属性</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #create(Class)}</td>
 *           <td>:</td>
 *           <td>构建BulkBean，针对一个类中的全部属性</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #map(Object, Object, boolean, String...)}</td>
 *           <td>:</td>
 *           <td>指定source中的值覆盖的装入到target中，使用一个布尔值区分是否忽略字段</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #map(Object, Object, String...)}</td>
 *           <td>:</td>
 *           <td>指定source中的值覆盖的装入到target中，默认使用提供的属性</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #mapAcquire(Object, Object, String...)}</td>
 *           <td>:</td>
 *           <td>指定source中的值覆盖的装入到target中，返回被填充的对象</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #newInstanceMap(Object, Class, String...)}</td>
 *           <td>:</td>
 *           <td>指定source中的值覆盖的装入到target中，先创建对象再填充</td>
 *       </tr>
 *       <tr>
 *           <td>{@link #mapCollectionAcquire(Collection, Class, String...)}</td>
 *           <td>:</td>
 *           <td>Collection中每一个元素都转化为目标对象</td>
 *       </tr>
 * </table>
 *
 * @author Create by liuwenhao on 2022/5/23 11:46
 */
@DerivedTypes
@SuppressWarnings("unused")
public class BulkTools {

    static Map<Object, BulkBean> map = new ConcurrentHashMap<>();

    static Map<String, List<FieldSignature>> fieldMap = new ConcurrentHashMap<>();

    static GenerateKey generateKey = (GenerateKey) KeyFactory.create(GenerateKey.class);

    /**
     * <h2>创建BulkBean</h2>
     * 条件：getter.length = setter.length = types.length
     *
     * @param target  类对象
     * @param getters getter方法名数组
     * @param setters setter方法名数组
     * @param types   类型数组
     * @return net.sf.cglib.beans.BulkBean
     * @author liuwenhao
     * @date 2022/5/24 13:34
     */
    public static BulkBean create(Class<?> target, String[] getters, String[] setters, Class<?>[] types) {
        checkLength(getters, setters, types);
        String simpleName = target.getSimpleName();
        String[] typeName = (String[]) Arrays.stream(types).map(Class::getSimpleName).toArray();
        Object key = generateKey.newInstance(simpleName, getters, setters, typeName);
        return map.computeIfAbsent(key, o -> BulkBean.create(target, getters, setters, types));
    }

    /**
     * <h2>创建BulkBean</h2>
     *
     * @param target 类对象
     * @param getter getter方法名集合
     * @param setter setter方法名集合
     * @param types  类型集合
     * @return net.sf.cglib.beans.BulkBean
     * @author liuwenhao
     * @date 2022/5/24 13:43
     */
    public static BulkBean create(Class<?> target, Set<String> getter, Set<String> setter, List<Class<?>> types) {
        String[] getterArr = getter.toArray(new String[0]);
        String[] setterArr = setter.toArray(new String[0]);
        Class<?>[] typeArr = types.toArray(new Class[0]);
        return create(target, getterArr, setterArr, typeArr);
    }

    /**
     * <h2>创建BulkBean</h2>
     *
     * @param target        需要创建BulkBean的类
     * @param isIgnore      true:仅解析数组，false:除数组外全解析
     * @param resolveFields 字段名数组
     * @return net.sf.cglib.beans.BulkBean
     * @author liuwenhao
     * @date 2022/5/24 14:22
     */
    public static BulkBean create(Class<?> target, boolean isIgnore, String... resolveFields) {
        String key = generateFieldKey(target.getName(), isIgnore, resolveFields);
        List<FieldSignature> signatures = fieldMap.computeIfAbsent(key, o -> resolveType(target, isIgnore, resolveFields));
        Set<String> getter = CollUtil.set(signatures, FieldSignature::getGetterName);
        Set<String> setter = CollUtil.set(signatures, FieldSignature::getSetterName);
        List<Class<?>> types = CollUtil.list(signatures, FieldSignature::getFileType);
        return create(target, getter, setter, types);
    }

    /**
     * <h2>创建BulkBean</h2>
     *
     * @param target 需要创建BulkBean的类
     * @return net.sf.cglib.beans.BulkBean
     * @author liuwenhao
     * @date 2022/5/24 14:22
     */
    public static BulkBean create(Class<?> target) {
        return create(target, false, "");
    }

    /**
     * <h2>指定属性copy</h2>
     * 指定source中的值覆盖的装入到target中<br>
     *
     * @param source    源对象
     * @param target    目标对象
     * @param isIgnore  true:仅解析数组，false:除数组外全解析
     * @param fieldName 字段名数组
     * @author liuwenhao
     * @date 2022/5/24 16:48
     */
    public static void map(Object source, Object target, boolean isIgnore, String... fieldName) {
        List<FieldSignature> sourceField = resolveType(source.getClass(), isIgnore, fieldName);
        List<FieldSignature> targetField = resolveType(target.getClass(), isIgnore, fieldName);
        boolean equal = CollUtil.equal(
            CollUtil.list(sourceField, FieldSignature::getFileType),
            CollUtil.list(targetField, FieldSignature::getFileType)
        );
        if (!equal) {
            throw TypeMismatchException.exception(
                "无法匹配类型 -- > {},{}",
                source.getClass().getName(),
                fieldName.getClass().getName()
            );
        }

        String[] getter = CollUtil.array(sourceField, FieldSignature::getGetterName);
        String[] setter = CollUtil.array(sourceField, FieldSignature::getSetterName);
        Class<?>[] types = CollUtil.array(sourceField, FieldSignature::getFileType);

        BulkBean sourceBulkBean = create(source.getClass(), getter, setter, types);
        BulkBean targetBulkBean = create(target.getClass(), getter, setter, types);
        targetBulkBean.setPropertyValues(target, sourceBulkBean.getPropertyValues(source));
    }

    /**
     * <h2>指定属性copy</h2>
     * 指定source中的值覆盖的装入到target中<br>
     * 默认拷贝fieldName中的属性
     *
     * @param source    源对象
     * @param target    目标对象
     * @param fieldName 字段名数组
     * @author liuwenhao
     * @date 2022/5/24 16:48
     */
    public static void map(Object source, Object target, String... fieldName) {
        map(source, target, true, fieldName);
    }

    /**
     * <h2>指定属性copy</h2>
     * 指定source中的值覆盖的装入到target中<br>
     * 默认拷贝fieldName中的属性
     *
     * @param source      源对象
     * @param targetClass 目标类对象
     * @param fieldName   字段名数组
     * @author liuwenhao
     * @date 2022/5/24 16:48
     */
    public static void newInstanceMap(Object source, Class<?> targetClass, String... fieldName) {
        ConstructorAccess<?> constructorAccess = ConstructorAccess.get(targetClass);
        Object newInstance = constructorAccess.newInstance();
        map(source, newInstance, fieldName);
    }

    /**
     * <h2>指定属性copy</h2>
     * 指定source中的值覆盖的装入到target中<br>
     * 通过泛型来确定源和目标的具体类型<br>
     * 默认拷贝fieldName中的属性
     *
     * @param source    源对象
     * @param target    目标类对象
     * @param fieldName 字段名数组
     * @author liuwenhao
     * @date 2022/5/24 16:48
     */
    public static <R, T> T mapAcquire(R source, T target, String... fieldName) {
        map(source, target, fieldName);
        return target;
    }

    /**
     * <h2>指定属性copy</h2>
     * Collection中每一个元素都转化为目标对象<br>
     * 默认拷贝fieldName中的属性
     *
     * @param rCollection 源对象集合
     * @param tClass      目标类型对象
     * @param fieldName   字段名数组
     * @return java.util.Collection<T>
     * @author liuwenhao
     * @date 2022/5/24 17:49
     */
    public static <R, T> Collection<T> mapCollectionAcquire(Collection<R> rCollection, Class<T> tClass, String... fieldName) {
        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
        return rCollection.stream().map(r -> {
            T tInstance = constructorAccess.newInstance();
            map(r, tInstance, fieldName);
            return tInstance;
        }).collect(Collectors.toList());
    }

    /**
     * <h2>验证</h2>
     * 对一批数组的长度进行验证要求其长度相等‘’
     *
     * @param objects 数组列表
     * @author liuwenhao
     * @date 2022/5/24 14:42
     */
    static void checkLength(Object[]... objects) {
        long count = Arrays.stream(objects).map(o -> o.length).distinct().count();
        if (count != 1) {
            throw UnequalLengthException.exception("getter数组、setter数组和类型数组的长度不对等");
        }
    }

    /**
     * <h2>解析</h2>
     * 解析后字段顺序与类文件中声明顺序一致
     *
     * @param type          类型对象
     * @param isIgnore      true:仅解析数组，false:除数组外全解析
     * @param resolveFields 字段名数组
     * @return java.util.List<com.deep.tool.beans.BulkTools.FieldSignature>
     * @author liuwenhao
     * @date 2022/5/24 14:48
     */
    static List<FieldSignature> resolveType(Class<?> type, boolean isIgnore, String... resolveFields) {
        List<String> refuseFieldList = Arrays.stream(resolveFields).collect(Collectors.toList());
        FieldAccess fieldAccess = FieldAccess.get(type);
        Field[] fields = fieldAccess.getFields();
        return Arrays.stream(fields).filter(s -> isIgnore == refuseFieldList.contains(s.getName())).map(s -> FieldSignature.build(type, s)).collect(Collectors.toList());
    }

    /**
     * <h2>通过类名和字段名生成一个字符串</h2>
     *
     * @param typeName     类名，全限定名
     * @param isIgnore     true:仅解析数组，false:除数组外全解析
     * @param refuseFields 忽略的字段名
     * @return java.lang.String
     * @author liuwenhao
     * @date 2022/5/24 16:01
     */
    static String generateFieldKey(String typeName, boolean isIgnore, String... refuseFields) {
        StringBuilder builder = new StringBuilder(typeName);
        for (String refuseField : refuseFields) {
            builder.append("-").append(refuseField);
        }
        return builder.append(isIgnore).toString();
    }

    interface GenerateKey {
        /**
         * <h2>生成key</h2>
         *
         * @param target  类名
         * @param getters getter方法名数组
         * @param setters setter方法名数组
         * @param types   类型名数组
         * @return java.lang.Object
         * @author liuwenhao
         * @date 2022/4/18 8:58
         */
        Object newInstance(String target, String[] getters, String[] setters, String[] types);
    }

    /**
     * <h2>字段签名</h2>
     *
     * @author Create by liuwenhao on 2022/5/23 11:46
     */
    @AllArgsConstructor
    @Data
    static class FieldSignature {

        /**
         * 字段
         */
        Field field;

        /**
         * 字段名
         */
        String fileName;

        /**
         * 字段类型
         */
        Class<?> fileType;

        /**
         * getter
         */
        String getterName;

        /**
         * setter
         */
        String setterName;

        public static FieldSignature build(Class<?> type, Field field) {
            try {
                String fieldName = field.getName();
                PropertyDescriptor beanDescriptor = new PropertyDescriptor(fieldName, type);
                Method readMethod = beanDescriptor.getReadMethod();
                Method writeMethod = beanDescriptor.getWriteMethod();
                return new FieldSignature(field, fieldName, field.getType(), readMethod.getName(), writeMethod.getName());
            } catch (IntrospectionException e) {
                throw MethodNotFondException.exception("getter方法后setter方法不存在 --> {}", field.getName());
            }
        }
    }

}