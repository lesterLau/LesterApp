package com.lesterlau.view.area;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 地区管理类
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class AreaConfig {

    private ArrayList<Province> provinces = new ArrayList<>();
    private ArrayList<Province> temp = new ArrayList<>();

    private volatile static AreaConfig instance = null;

    private AreaConfig() {
    }

    public static AreaConfig getInstance() {
        if (instance == null) {
            synchronized (AreaConfig.class) {
                if (instance == null) {
                    instance = new AreaConfig();
                    instance.parseJson(getFromAssets(Utils.getApp(), "info_cities.json"));
                }
            }
        }
        return instance;
    }

    public void parseJson(String s) {
        if (TextUtils.isEmpty(s)) return;
        try {
            provinces = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(s);
            if (jsonArray == null) {
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                Province province = new Province();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                province.setProvince(jsonObject.optString("province"));
                province.setProvinceName(jsonObject.optString("provinceName"));
                province.setProvinceID(jsonObject.optInt("provinceID"));
                JSONArray cityJsonArray = jsonObject.optJSONArray("cities");
                if (cityJsonArray != null) {
                    ArrayList<City> cityList = new ArrayList<>();
                    for (int j = 0; j < cityJsonArray.length(); j++) {
                        City city = new City();
                        JSONObject cityJsonObject = cityJsonArray.getJSONObject(i);
                        city.setCity(cityJsonObject.optString("city"));
                        city.setCityName(cityJsonObject.optString("cityName"));
                        city.setCityID(cityJsonObject.optInt("cityID"));
                        city.setProvince(cityJsonObject.optString("province"));
                        city.setProvinceName(cityJsonObject.optString("provinceName"));
                        city.setProvinceID(cityJsonObject.optInt("provinceID"));
                        cityList.add(city);
                    }
                    province.setCities(cityList);
                }
                provinces.add(province);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从assets里边读取字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =================初始化完成==================

    /**
     * @return 获取json原始串，测试时调用，正常使用请勿调用
     */
    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    /**
     * 根据省市id获取当前的city
     *
     * @param provinceName 省份名称
     * @param cityName     城市名称
     * @return City
     */
    public City getCity(String provinceName, String cityName) {
        City city = new City();
        for (Province pro : provinces) {
            if (pro.getProvinceName().equals(provinceName)) {
                ArrayList<City> cityList = pro.getCities();
                for (City cit : cityList) {
                    if (cityName.equals(cit.getCityName())) {
                        city = cit;
                        break;
                    }
                }
            }
        }
        return city;
    }
}
