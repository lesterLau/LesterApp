package com.lesterlau.http;

/**
 * Created by liubin on 2018/4/23 0023.
 */
public class ApiException extends RuntimeException {

    private int errorCode;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
