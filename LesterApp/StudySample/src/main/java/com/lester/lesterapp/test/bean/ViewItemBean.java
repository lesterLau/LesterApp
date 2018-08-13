package com.lester.lesterapp.test.bean;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class ViewItemBean {
    private String name;
    private String desc;
    private Class cls;

    public ViewItemBean(String name, String desc, Class cls) {
        this.name = name;
        this.desc = desc;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
