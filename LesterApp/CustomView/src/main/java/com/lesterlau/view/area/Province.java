package com.lesterlau.view.area;


import java.util.ArrayList;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class Province {

    private String province;
    private String provinceName;
    private int provinceID;
    private ArrayList<City> cities;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }
}
