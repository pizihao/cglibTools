package com.deep.tool.beans;

import com.deep.tool.annotations.DerivedTypes;
import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.core.KeyFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2>Bulk</h2>
 *
 * @author Create by liuwenhao on 2022/5/23 11:46
 */
@DerivedTypes
public class BulkTools {

    static Map<Object, BulkBean> map = new ConcurrentHashMap<>();

    static GenerateKey generateKey = (GenerateKey) KeyFactory.create(GenerateKey.class);


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

}