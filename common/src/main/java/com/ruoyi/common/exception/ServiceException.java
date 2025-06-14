package com.ruoyi.common.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}