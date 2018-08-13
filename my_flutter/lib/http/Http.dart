import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class Api {
  static const String BaseUrl = "http://www.wanandroid.com/";

  //首页banner
  static const String BANNER = "banner/json";

  //首页文章列表 http://www.wanandroid.com/article/list/0/json
  // 知识体系下的文章http://www.wanandroid.com/article/list/0/json?cid=60
  static const String ARTICLE_LIST = "article/list/";

  //收藏文章列表
  static const String COLLECT_LIST = "lg/collect/list/";

  //搜索
  static const String ARTICLE_QUERY = "article/query/";

  //收藏,取消收藏
  static const String COLLECT = "lg/collect/";
  static const String UNCOLLECT_ORIGINID = "lg/uncollect_originId/";

  //我的收藏列表中取消收藏
  static const String UNCOLLECT_LIST = "lg/uncollect/";

  //登录,注册
  static const String LOGIN = "user/login";
  static const String REGISTER = "user/register";

  //知识体系
  static const String TREE = "tree/json";

  //常用网站
  static const String FRIEND = "friend/json";

  //搜索热词
  static const String HOTKEY = "hotkey/json";
}

class HttpUtil {
  static const String GET = "get";
  static const String POST = "post";

  static void get(String url, Function callback,
      {Map<String, String> params,
      Map<String, String> headers,
      Function errorCallback}) async {
    if (!url.startsWith("http")) {
      url = Api.BaseUrl + url;
    }
    if (params != null && params.isNotEmpty) {
      StringBuffer sb = new StringBuffer("?");
      params.forEach((key, value) {
        sb.write("$key" + "=" + "$value" + "&");
      });
      String paramStr = sb.toString().substring(0, sb.length - 1);
      url += paramStr;
    }
    await doRequest(url, callback,
        method: GET, headers: headers, errorCallback: errorCallback);
  }

  static void post(String url, Function callback,
      {Map<String, String> params,
      Map<String, String> headers,
      Function errorCallback}) async {
    if (!url.startsWith("http")) {
      url = Api.BaseUrl + url;
    }
    await doRequest(url, callback,
        method: POST,
        params: params,
        headers: headers,
        errorCallback: errorCallback);
  }

  static doRequest(String url, Function callback,
      {String method,
      Map<String, String> params,
      Map<String, String> headers,
      Function errorCallback}) async {
    String errorMsg;
    int errorCode;
    var data;
    if (headers == null) {
      headers = new Map();
    }
    if (params == null) {
      params = new Map();
    }
    SharedPreferences sp = await SharedPreferences.getInstance();
    String cookie = sp.get("cookie");
    if (cookie != null && cookie.length > 0) {
      headers['Cookie'] = cookie;
    }
    http.Response res;
    if (POST == method) {
      print("POST:URL=" + url);
      print("POST:BODY=" + params.toString());
      res = await http.post(url, headers: headers, body: params);
    } else {
      print("GET:URL=" + url);
      res = await http.get(url, headers: headers);
    }
    if (res.statusCode != 200) {
      errorMsg = "网络请求错误，状态码：" + res.statusCode.toString();
      if (errorCallback != null) {
        errorCallback(errorMsg);
      }
      return;
    }
    Map<String, dynamic> map = json.decode(res.body);
    errorMsg = map['errorMsg'];
    errorCode = map['errorCode'];
    data = map['data'];
    if (url.contains(Api.LOGIN)) {
      SharedPreferences sp = await SharedPreferences.getInstance();
      sp.setString("cookie", res.headers['set-cookie']);
    }
    if (callback != null) {
      if (errorCode >= 0) {
        callback(data);
      } else {
        if (errorCallback != null) {
          errorCallback(errorMsg);
        }
      }
    }
  }
}
