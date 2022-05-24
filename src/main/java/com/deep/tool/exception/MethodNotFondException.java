package com.deep.tool.exception;

import com.deep.tool.util.StrUtil;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/5/24 15:47
 */
public class MethodNotFondException  extends RuntimeException {
    private static final long serialVersionUID = 4220666905005394823L;

    public MethodNotFondException() {
        super();
    }

    public MethodNotFondException(Throwable throwable) {
        super(throwable);
    }

    public MethodNotFondException(String message) {
        super(message);
    }

    public static MethodNotFondException exception(String message) {
        return new MethodNotFondException(message);
    }

    public static MethodNotFondException exception(Throwable throwable) {
        return new MethodNotFondException(throwable);
    }

    public static MethodNotFondException exception(String format, Object... elements) {
        return new MethodNotFondException(StrUtil.format(format, elements));
    }

    public static MethodNotFondException of(String msg) {
        throw MethodNotFondException.exception(msg);
    }

    public static MethodNotFondException of(String format, Object... elements) {
        throw MethodNotFondException.exception(format, elements);
    }

    public static MethodNotFondException of(Throwable throwable) {
        throw MethodNotFondException.exception(throwable);
    }
}