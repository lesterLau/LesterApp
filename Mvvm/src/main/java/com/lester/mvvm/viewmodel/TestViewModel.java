package com.lester.mvvm.viewmodel;

import android.app.Activity;

import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;

/**
 * @Author lester
 * @Date 2018/7/27
 */
public class TestViewModel extends BaseViewModel {
    private String username;
    private String nickname;
    private String age;

    public TestViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public void loadData() {
        username = "张三";
        nickname = "张三的昵称";
        age = "18";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
