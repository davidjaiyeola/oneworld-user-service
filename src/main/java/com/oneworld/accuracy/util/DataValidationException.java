package com.oneworld.accuracy.util;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String msg) {
        super(msg);
    }

    public DataValidationException(String msg, Throwable t) {
        super(msg, t);
    }
}
