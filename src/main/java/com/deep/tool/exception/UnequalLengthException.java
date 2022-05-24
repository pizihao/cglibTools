package com.deep.tool.exception;

import com.deep.tool.util.StrUtil;

/**
 * <h2>数组长度不对等异常</h2>
 *
 * @author Create by liuwenhao on 2022/5/24 13:55
 */
public class UnequalLengthException extends RuntimeException {
    private static final long serialVersionUID = 4220666905005394823L;

    public UnequalLengthException() {
        super();
    }

    public UnequalLengthException(Throwable throwable) {
        super(throwable);
    }

    public UnequalLengthException(String message) {
        super(message);
    }

    public static UnequalLengthException exception(String message) {
        return new UnequalLengthException(message);
    }

    public static UnequalLengthException exception(Throwable throwable) {
        return new UnequalLengthException(throwable);
    }

    public static UnequalLengthException exception(String format, Object... elements) {
        return new UnequalLengthException(StrUtil.format(format, elements));
    }

    public static UnequalLengthException of(String msg) {
        throw UnequalLengthException.exception(msg);
    }

    public static UnequalLengthException of(String format, Object... elements) {
        throw UnequalLengthException.exception(format, elements);
    }

    public static UnequalLengthException of(Throwable throwable) {
        throw UnequalLengthException.exception(throwable);
    }
}