package com.deep.tool.annotations;

import java.lang.annotation.*;

/**
 * <h2>标注是继承类型</h2>
 * 不会对原功能添加扩展
 *
 * @author Create by liuwenhao on 2022/5/21 17:44
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DerivedTypes {
}