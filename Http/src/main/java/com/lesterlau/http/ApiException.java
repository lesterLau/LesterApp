package com.lesterlau.http;
/**
 * Created by  lesterlau on 2018/4/9.
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
