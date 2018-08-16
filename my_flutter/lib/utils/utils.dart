import 'dart:async';
import 'dart:convert';

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

  static StringBuffer getStringBuffer(
    Object content, {
    Object obj,
    Object obj1,
    Object obj2,
    Object obj3,
    Object obj4,
    Object obj5,
    Object obj6,
    Object obj7,
    Object obj8,
    Object obj9,
    Object obj10,
  }) {
    StringBuffer sb = new StringBuffer(content);
    if (obj != null) sb.write(obj);
    if (obj1 != null) sb.write(obj1);
    if (obj2 != null) sb.write(obj2);
    if (obj3 != null) sb.write(obj3);
    if (obj4 != null) sb.write(obj4);
    if (obj5 != null) sb.write(obj5);
    if (obj6 != null) sb.write(obj6);
    if (obj7 != null) sb.write(obj7);
    if (obj8 != null) sb.write(obj8);
    if (obj9 != null) sb.write(obj9);
    if (obj10 != null) sb.write(obj10);
    return sb;
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

  static Future<bool> isLogin() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    bool b = sp.getBool(_isLogin);
    return true == b;
  }
}

class OSCDataUtils {
  static final String SP_AC_TOKEN = "accessToken";
  static final String SP_RE_TOKEN = "refreshToken";
  static final String SP_UID = "uid";
  static final String SP_IS_LOGIN = "OSCisLogin";
  static final String SP_EXPIRES_IN = "expiresIn";
  static final String SP_TOKEN_TYPE = "tokenType";

  static final String SP_USER_NAME = "name";
  static final String SP_USER_ID = "id";
  static final String SP_USER_LOC = "location";
  static final String SP_USER_GENDER = "gender";
  static final String SP_USER_AVATAR = "avatar";
  static final String SP_USER_EMAIL = "email";
  static final String SP_USER_URL = "url";

  // 保存用户登录信息，data中包含了token等信息
  static saveLoginInfo(Map data) async {
    print(StringUtils.getStringBuffer("saveLoginInfo==>", obj: data));
    if (data != null) {
      SharedPreferences sp = await SharedPreferences.getInstance();
      String accessToken = data['access_token'];
      await sp.setString(SP_AC_TOKEN, accessToken);
      String refreshToken = data['refresh_token'];
      await sp.setString(SP_RE_TOKEN, refreshToken);
      num uid = data['uid'];
      await sp.setInt(SP_UID, uid);
      String tokenType = data['tokenType'];
      await sp.setString(SP_TOKEN_TYPE, tokenType);
      num expiresIn = data['expires_in'];
      await sp.setInt(SP_EXPIRES_IN, expiresIn);

      await sp.setBool(SP_IS_LOGIN, true);
    }
  }

  static clearLoginInfo() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    await sp.setString(SP_AC_TOKEN, "");
    await sp.setString(SP_RE_TOKEN, "");
    await sp.setInt(SP_UID, -1);
    await sp.setString(SP_TOKEN_TYPE, "");
    await sp.setInt(SP_EXPIRES_IN, -1);
    await sp.setBool(SP_IS_LOGIN, false);
  }

  // 保存用户个人信息
  static Future<UserInfo> saveUserInfo(Map data) async {
    if (data != null) {
      SharedPreferences sp = await SharedPreferences.getInstance();
      String name = data['name'];
      num id = data['id'];
      String gender = data['gender'];
      String location = data['location'];
      String avatar = data['avatar'];
      String email = data['email'];
      String url = data['url'];
      await sp.setString(SP_USER_NAME, name);
      await sp.setInt(SP_USER_ID, id);
      await sp.setString(SP_USER_GENDER, gender);
      await sp.setString(SP_USER_AVATAR, avatar);
      await sp.setString(SP_USER_LOC, location);
      await sp.setString(SP_USER_EMAIL, email);
      await sp.setString(SP_USER_URL, url);
      UserInfo userInfo = new UserInfo(
          id: id,
          name: name,
          gender: gender,
          avatar: avatar,
          email: email,
          location: location,
          url: url);
      return userInfo;
    }
    return null;
  }

  // 获取用户信息
  static Future<UserInfo> getUserInfo() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    bool isLogin = sp.getBool(SP_IS_LOGIN);
    if (isLogin == null || !isLogin) {
      return null;
    }
    UserInfo userInfo = new UserInfo();
    userInfo.id = sp.getInt(SP_USER_ID);
    userInfo.name = sp.getString(SP_USER_NAME);
    userInfo.avatar = sp.getString(SP_USER_AVATAR);
    userInfo.email = sp.getString(SP_USER_EMAIL);
    userInfo.location = sp.getString(SP_USER_LOC);
    userInfo.gender = sp.getString(SP_USER_GENDER);
    userInfo.url = sp.getString(SP_USER_URL);
    return userInfo;
  }

  static Future<bool> isLogin() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    bool b = sp.getBool(SP_IS_LOGIN);
    return b != null && b;
  }

  static Future<String> getAccessToken() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    return sp.getString(SP_AC_TOKEN);
  }
}

// 用户信息
class UserInfo {
  String gender;
  String name;
  String location;
  num id;
  String avatar;
  String email;
  String url;

  UserInfo(
      {this.id,
      this.name,
      this.gender,
      this.avatar,
      this.email,
      this.location,
      this.url});

  @override
  String toString() {
    return 'UserInfo{gender: $gender, name: $name, location: $location, id: $id, avatar: $avatar, email: $email, url: $url}';
  }
}

// 黑名单工具类，用于在本地操作黑名单
class BlackListUtils {
  static final String SP_BLACK_LIST = "blackList";

  // 将对象数组转化为整型数组
  static List<int> convert(List objList) {
    if (objList == null || objList.isEmpty) {
      return new List<int>();
    }
    List<int> intList = new List();
    for (var obj in objList) {
      intList.add(obj['authorid']);
    }
    return intList;
  }

  // 字符串转化为整型数组
  static List<int> _str2intList(String str) {
    if (str != null && str.length > 0) {
      List<String> list = str.split(",");
      if (list != null && list.isNotEmpty) {
        List<int> intList = new List();
        for (String s in list) {
          intList.add(int.parse(s));
        }
        return intList;
      }
    }
    return null;
  }

  // 整型数组转化为字符串
  static String _intList2Str(List<int> list) {
    if (list == null || list.isEmpty) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (int id in list) {
      sb.write("$id,");
    }
    String result = sb.toString();
    return result.substring(0, result.length - 1);
  }

  // 保存黑名单的id
  static Future<String> saveBlackListIds(List<int> list) async {
    String str = _intList2Str(list);
    if (str != null) {
      SharedPreferences sp = await SharedPreferences.getInstance();
      sp.setString(SP_BLACK_LIST, str);
    } else {
      SharedPreferences sp = await SharedPreferences.getInstance();
      sp.setString(SP_BLACK_LIST, "");
    }
    return str;
  }

  // 获取本地保存的黑名单id数据
  static Future<List<int>> getBlackListIds() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    String str = sp.getString(SP_BLACK_LIST);
    if (str != null && str.length > 0) {
      return _str2intList(str);
    }
    return null;
  }

  // 向黑名单中添加一个id
  static Future<List<int>> addBlackId(int id) async {
    List<int> list = await getBlackListIds();
    if (list != null && list.isNotEmpty) {
      if (!list.contains(id)) {
        list.add(id);
        String str = await saveBlackListIds(list);
        return _str2intList(str);
      } else {
        return list;
      }
    } else {
      List<int> l = new List();
      l.add(id);
      String str = await saveBlackListIds(l);
      return _str2intList(str);
    }
  }

  // 向黑名单中移除一个id
  static Future<List<int>> removeBlackId(int id) async {
    List<int> list = await getBlackListIds();
    if (list != null && list.isNotEmpty) {
      if (list.contains(id)) {
        list.remove(id);
        String str = await saveBlackListIds(list);
        return _str2intList(str);
      }
    }
    return list;
  }
}

class Utf8Utils {
  static String encode(String origin) {
    if (origin == null || origin.length == 0) {
      return null;
    }
    List<int> list = utf8.encode(origin);
    StringBuffer sb = new StringBuffer();
    for (int i in list) {
      sb.write("$i,");
    }
    String result = sb.toString();
    return result.substring(0, result.length - 1);
  }

  static String decode(String encodeStr) {
    if (encodeStr == null || encodeStr.length == 0) {
      return null;
    }
    List<String> list = encodeStr.split(",");
    if (list != null && list.isNotEmpty) {
      List<int> intList = new List();
      for (String s in list) {
        intList.add(int.parse(s));
      }
      return utf8.decode(intList);
    }
    return null;
  }
}
