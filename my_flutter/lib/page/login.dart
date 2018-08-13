import 'package:flutter/material.dart';
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
  TextEditingController _namTEC = new TextEditingController(text: 'canhuah',);
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
            Constants.eventBus.fire(new LoginEvent());
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
            Constants.eventBus.fire(new LoginEvent());
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

class LoginEvent {}
