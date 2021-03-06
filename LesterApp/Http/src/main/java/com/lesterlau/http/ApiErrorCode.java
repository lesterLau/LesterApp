package com.lesterlau.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liubin on 2018/4/23 0023.
 */
public enum ApiErrorCode {
    ERROR_UNKNOWN(1, "未知错误,错误信息："),
    ERROR_NETWORK(2, "网络错误，请检查你的网络设置"),
    ERROR_HOST(3, "服务器地址错误，请检查服务器地址是否正常,错误信息："),
    ERROR_IO(4, "网络请求数据传输异常,错误信息："),
    ERROR_CONNECTION_TIMEOUT(5, "网络连接超时，请检查你的网络设置,错误信息："),
    ERROR_500(500, "服务器错误,错误信息："),
    ERROR_404(404, "地址错误,错误信息："),
    HOST_KEY(1001, "host_url"),
    HOST_WAN_VALUE(1002, "http://www.wanandroid.com/"),
    LAST(-1, "占位");
    // 成员变量
    private String value;
    private int code;

    // 构造方法
    ApiErrorCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private static Map<Integer, ApiErrorCode> typeIdMap = new HashMap<>();
    private static Map<String, ApiErrorCode> typeNameMap = new HashMap<>();

    static {
        for (ApiErrorCode typeEnum : ApiErrorCode.values()) {
            typeIdMap.put(typeEnum.code, typeEnum);
            typeNameMap.put(typeEnum.getValue().toLowerCase(), typeEnum);
        }
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    public static ApiErrorCode parseCode(int code) {
        return typeIdMap.get(code);
    }

}

