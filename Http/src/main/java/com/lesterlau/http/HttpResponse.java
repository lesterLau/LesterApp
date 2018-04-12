package com.lesterlau.http;

import java.util.List;

/**
 * Created by  lesterlau on 2018/4/9.
 */

public class HttpResponse<T, K> {
    private boolean result;
    private int code;
    private String msg;
    private List<K> dataList;
    private T data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
}
