import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/page/home.dart';
import 'package:my_flutter/page/my.dart';
import 'package:my_flutter/page/search.dart';
import 'package:my_flutter/page/tree.dart';
import 'package:package_info/package_info.dart';

Future<void> main() async {
  final PackageInfo packageInfo = await PackageInfo.fromPlatform();
  runApp(Directionality(
      textDirection: TextDirection.ltr, child: Router(packageInfo)));
}

class Router extends StatelessWidget {
  Router(this.packageInfo);

  final PackageInfo packageInfo;

  @override
  Widget build(BuildContext context) {
    return new MyApp();
  }
}

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new MyAppState();
  }
}

class MyAppState extends State<MyApp> with TickerProviderStateMixin {
  int _tabIndex = 0;

  List<BottomNavigationBarItem> _navigationViews;

  var _appBarTitles = ['首页', '发现', '我的'];

  var _body;

  final _navigationKey = GlobalKey<NavigatorState>();

  initData() {
    _navigationViews = <BottomNavigationBarItem>[
      new BottomNavigationBarItem(
          icon: const Icon(Icons.home),
          title: new Text(_appBarTitles[0]),
          backgroundColor: Colors.blue),
      new BottomNavigationBarItem(
          icon: const Icon(Icons.widgets),
          title: new Text(_appBarTitles[1]),
          backgroundColor: Colors.blue),
      new BottomNavigationBarItem(
          icon: const Icon(Icons.person),
          title: new Text(_appBarTitles[2]),
          backgroundColor: Colors.blue)
    ];
    _body = new IndexedStack(
      children: <Widget>[new HomePage(), new TreePage(), new MyInfoPage()],
      index: _tabIndex,
    );
  }

  @override
  Widget build(BuildContext context) {
    initData();
    return new MaterialApp(
      navigatorKey: _navigationKey,
      theme: new ThemeData(
          primaryColor: AppColors.colorPrimary,
          accentColor: AppColors.accentColor),
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text(_appBarTitles[_tabIndex],
              style: new TextStyle(color: Colors.white, fontSize: 18.0)),
          actions: <Widget>[
            new IconButton(
                icon: new Icon(Icons.search),
                onPressed: () {
                  _navigationKey.currentState
                      .push(new MaterialPageRoute(builder: (context) {
                    return new SearchPage(null);
                  }));
                })
          ],
        ),
        body: _body,
        bottomNavigationBar: new BottomNavigationBar(
          items: _navigationViews
              .map((BottomNavigationBarItem navigationView) => navigationView)
              .toList(),
          currentIndex: _tabIndex,
          type: BottomNavigationBarType.fixed,
          onTap: (index) {
            setState(() {
              _tabIndex = index;
            });
          },
        ),
      ),
    );
  }
}
