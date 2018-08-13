import 'dart:async';

import 'package:flutter/widgets.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:shared_preferences/shared_preferences.dart';

class StringUtils {
  static TextSpan getTextSpan(String text, String key) {
    if (text == null || key == null) {
      return null;
    }

    if (text == null || key == null) {
      return null;
    }

    String splitString1 = "<em class='highlight'>";
    String splitString2 = "</em>";

    String textOrigin =
        text.replaceAll(splitString1, '').replaceAll(splitString2, '');

    TextSpan textSpan = new TextSpan(
        text: key, style: new TextStyle(color: AppColors.colorPrimary));

    List<String> split = textOrigin.split(key);

    List<TextSpan> list = new List<TextSpan>();

    for (int i = 0; i < split.length; i++) {
      list.add(new TextSpan(text: split[i]));
      list.add(textSpan);
    }

    list.removeAt(list.length - 1);

    return new TextSpan(children: list);
  }
}

class DataUtils {
  static final String _isLogin = "isLogin";
  static final String _userName = "userName";

  // 保存用户登录信息，data中包含了userName
  static Future saveLoginInfo(String userName) async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    await sp.setString(_userName, userName);
    await sp.setBool(_isLogin, true);
  }

  static Future clearLoginInfo() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.clear();
  }

  static Future<String> getUserName() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.getString(_userName);
  }

  static Future<bool> login() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    bool b = sp.getBool(_isLogin);
    return true == b;
  }
}
