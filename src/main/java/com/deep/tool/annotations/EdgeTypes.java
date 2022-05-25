package com.deep.tool.annotations;

import java.lang.annotation.*;

/**
 * <h2>边缘扩展</h2>
 * 更倾向于让使用更加方便
 *
 * @author Create by liuwenhao on 2022/5/25 17:19
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EdgeTypes {
}