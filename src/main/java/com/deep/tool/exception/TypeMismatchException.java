package com.deep.tool.exception;

import com.deep.tool.util.StrUtil;

/**
 * <h2>类型不匹配异常</h2>
 *
 * @author Create by liuwenhao on 2022/5/24 17:26
 */
public class TypeMismatchException  extends RuntimeException {
    private static final long serialVersionUID = 4220666905005394823L;

    public TypeMismatchException() {
        super();
    }

    public TypeMismatchException(Throwable throwable) {
        super(throwable);
    }

    public TypeMismatchException(String message) {
        super(message);
    }

    public static TypeMismatchException exception(String message) {
        return new TypeMismatchException(message);
    }

    public static TypeMismatchException exception(Throwable throwable) {
        return new TypeMismatchException(throwable);
    }

    public static TypeMismatchException exception(String format, Object... elements) {
        return new TypeMismatchException(StrUtil.format(format, elements));
    }

    public static TypeMismatchException of(String msg) {
        throw TypeMismatchException.exception(msg);
    }

    public static TypeMismatchException of(String format, Object... elements) {
        throw TypeMismatchException.exception(format, elements);
    }

    public static TypeMismatchException of(Throwable throwable) {
        throw TypeMismatchException.exception(throwable);
    }
}