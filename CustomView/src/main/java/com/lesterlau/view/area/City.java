package com.lesterlau.view.area;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class City {

    private String city;
    private String cityName;
    private int cityID;
    private String province;
    private String provinceName;
    private int provinceID;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

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

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityID=" + cityID +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", provinceID=" + provinceID +
                '}';
    }
}
