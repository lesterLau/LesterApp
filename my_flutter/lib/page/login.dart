import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/utils/utils.dart';

class LoginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new LoginPageState();
  }
}

class LoginPageState extends State<LoginPage> {
  TextEditingController _namTEC = new TextEditingController(
    text: 'canhuah',
  );
  TextEditingController _pwdTEC = new TextEditingController(text: 'a123456');
  GlobalKey<ScaffoldState> _globalKey;

  @override
  void initState() {
    super.initState();
    _globalKey = new GlobalKey<ScaffoldState>();
  }

  @override
  Widget build(BuildContext context) {
    Row icon = new Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        new Icon(
          Icons.account_circle,
          color: Theme.of(context).accentColor,
          size: 80.0,
        )
      ],
    );
    TextField name = new TextField(
      autocorrect: true,
      decoration: new InputDecoration(labelText: "用户名："),
      controller: _namTEC,
    );
    TextField pwd = new TextField(
      obscureText: true,
      decoration: new InputDecoration(labelText: "密码："),
      controller: _pwdTEC,
    );
    Row loginAndRegister = new Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        new RaisedButton(
            child: new Text(
              "登陆",
              style: new TextStyle(color: Colors.white),
            ),
            color: Theme.of(context).accentColor,
            disabledColor: Colors.grey,
            textColor: Colors.white,
            onPressed: () {
              _login();
            }),
        new RaisedButton(
            child: new Text(
              "注册",
              style: new TextStyle(color: Colors.white),
            ),
            color: Colors.blue,
            disabledColor: Colors.grey,
            textColor: Colors.white,
            onPressed: () {
              _register();
            })
      ],
    );
    return new Scaffold(
      key: _globalKey,
      appBar: new AppBar(
        title: new Text("登陆"),
      ),
      body: new Padding(
        padding: EdgeInsets.fromLTRB(40.0, 10.0, 40.0, 10.0),
        child: new ListView(
          children: <Widget>[
            icon,
            name,
            pwd,
            new Padding(padding: EdgeInsets.fromLTRB(40.0, 10.0, 40.0, 10.0)),
            loginAndRegister
          ],
        ),
      ),
    );
  }

  void _login() {
    String name = _namTEC.text;
    String pwd = _pwdTEC.text;
    if (name == null || name.length == 0) {
      _showMessage('请先输入姓名');
      return;
    }
    if (pwd == null || pwd.length == 0) {
      _showMessage('请先输入密码');
      return;
    }
    Map<String, String> map = new Map();
    map['username'] = name;
    map['password'] = pwd;
    HttpUtil.post(
        Api.LOGIN,
        (data) async {
          DataUtils.saveLoginInfo(name).then((r) {
            Constants.eventBus.fire(new LoginEvent(true));
            Navigator.of(context).pop();
          });
        },
        params: map,
        errorCallback: (msg) {
          _showMessage(msg);
        });
  }

  void _register() {
    String name = _namTEC.text;
    String pwd = _pwdTEC.text;
    if (name == null || name.length == 0) {
      _showMessage('请先输入姓名');
      return;
    }
    if (pwd == null || pwd.length == 0) {
      _showMessage('请先输入密码');
      return;
    }
    Map<String, String> map = new Map();
    map['username'] = name;
    map['password'] = pwd;
    map['repassword'] = pwd;
    HttpUtil.post(
        Api.REGISTER,
        (data) async {
          DataUtils.saveLoginInfo(name).then((r) {
            Constants.eventBus.fire(new LoginEvent(true));
            Navigator.of(context).pop();
          });
        },
        params: map,
        errorCallback: (msg) {
          _showMessage(msg);
        });
  }

  void _showMessage(String msg) {
    _globalKey.currentState.showSnackBar(new SnackBar(content: new Text(msg)));
  }
}

class OSCLoginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new OSCLoginPageState();
  }
}

class OSCLoginPageState extends State<OSCLoginPage> {
  int _count = 0;
  final int _maxCount = 5;
  bool _loading = true;
  GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey();
  StreamSubscription _onUrlChanged;
  StreamSubscription<WebViewStateChanged> _onStateChanged;
  FlutterWebviewPlugin _flutterWebviewPlugin = new FlutterWebviewPlugin();

  @override
  void initState() {
    super.initState();
    _onStateChanged = _flutterWebviewPlugin.onStateChanged.listen((state) {});
    _onUrlChanged = _flutterWebviewPlugin.onUrlChanged.listen((url) {
      setState(() {
        _loading = false;
      });
      if (url != null && url.length > 0 && url.contains("osc/osc.php?code=")) {
        new Timer(const Duration(seconds: 1), parseResult);
      }
    });
  }

  @override
  void dispose() {
    _onUrlChanged.cancel();
    _onStateChanged.cancel();
    _flutterWebviewPlugin.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> titleContent = [];
    titleContent.add(new Text(
      "登陆中国开源",
      style: new TextStyle(color: Colors.white),
    ));
    if (_loading) {
      titleContent.add(new CircularProgressIndicator());
    }
    titleContent.add(new CupertinoActivityIndicator());
    titleContent.add(new Container(
      width: 50.0,
    ));
    return new WebviewScaffold(
      key: _scaffoldKey,
      url: Api.LOGIN_URL,
      appBar: new AppBar(
        title: new Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: titleContent,
        ),
        iconTheme: new IconThemeData(color: Colors.white),
      ),
      withZoom: true,
      withLocalStorage: true,
      withJavascript: true,
    );
  }

  void parseResult() {
    if (_count > _maxCount) {
      return;
    }
    _flutterWebviewPlugin.evalJavascript("get();").then((result) {
      _count++;
      print("_count==================>");
      print(_count);
      print("result==================>");
      print(result);
      if (result != null && result.length > 0) {
        var map = json.decode(result);
        if (map is String) {
          map = json.decode(map);
        }
        if (map != null) {
          OSCDataUtils.saveLoginInfo(map);
          Constants.eventBus.fire(new LoginEvent(true));
          Navigator.of(context).pop();
        }
      } else {
        new Timer(const Duration(seconds: 1), parseResult);
      }
    });
  }
}

class LoginEvent {
  bool isLogin;

  LoginEvent(this.isLogin);

  @override
  String toString() {
    return 'LoginEvent{isLogin: $isLogin}';
  }
}
