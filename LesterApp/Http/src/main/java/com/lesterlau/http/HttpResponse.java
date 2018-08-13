package com.lesterlau.http;

import java.util.List;

/**
 * Created by liubin on 2016/12/16.
 */

public class HttpResponse<T, K> {
    private boolean result;
    private int code = -1;
    private int errorCode = -1;
    private String message;
    private String msg;
    private String errorMsg;
    private List<K> dataList;
    private T data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        if (code == -1) {
            code = errorCode;
        }
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<K> getDataList() {
        return dataList;
    }

    public void setDataList(List<K> dataList) {
        this.dataList = dataList;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        if (msg == null || msg.length() == 0) {
            msg = message;
        }
        if (msg == null || msg.length() == 0) {
            msg = errorMsg;
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "result=" + result +
                ", code=" + code +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", msg='" + msg + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", dataList=" + dataList +
                ", data=" + data +
                '}';
    }
}
