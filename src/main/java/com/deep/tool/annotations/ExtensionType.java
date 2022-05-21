package com.deep.tool.annotations;

import java.lang.annotation.*;

/**
 * <h2>标注是扩展类型</h2>
 * 存在于原方案完全不同的扩展
 *
 * @author Create by liuwenhao on 2022/4/18 4:58
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionType {
}
