package com.db.javaschool.tdd.babakov.homework;

public class CycleFoundException extends Exception {
    protected CycleFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CycleFoundException(Throwable cause) {
        super(cause);
    }

    public CycleFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CycleFoundException(String message) {
        super(message);
    }

    public CycleFoundException() {
        super();
    }
}
