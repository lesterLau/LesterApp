package com.lester.lesterapp.observe;

/**
 * 抛出数据处理实体类
 *
 * @Author lester
 * @Date 2018/7/24
 */
public class Msg {

    private String key;
    private Object data;

    public Msg(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "key='" + key + '\'' +
                ", data=" + String.valueOf(data) +
                '}';
    }
}
