import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/page/my.dart';
import 'package:my_flutter/page/news.dart';
import 'package:my_flutter/page/tweet.dart';

class MyOSCApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new MyOSCAppState();
  }
}

class MyOSCAppState extends State<MyOSCApp> {
  int _tabIndex = 0;
  final _tabTextStyleNormal = new TextStyle(color: Colors.grey);
  final _tabTextStyleSelected = new TextStyle(color: AppColors.accentColor);

  var _tabImages;
  var _body;
  var _appBarTitles = ['资讯', '动弹', '发现', '我的'];

  Image getTabImage(String path) {
    return new Image.asset(
      path,
      width: 20.0,
      height: 20.0,
    );
  }

  void initData() {
    _tabImages = [
      [
        getTabImage('images/ic_nav_news_normal.png'),
        getTabImage('images/ic_nav_news_actived.png')
      ],
      [
        getTabImage('images/ic_nav_tweet_normal.png'),
        getTabImage('images/ic_nav_tweet_actived.png')
      ],
      [
        getTabImage('images/ic_nav_discover_normal.png'),
        getTabImage('images/ic_nav_discover_actived.png')
      ],
      [
        getTabImage('images/ic_nav_my_normal.png'),
        getTabImage('images/ic_nav_my_pressed.png')
      ]
    ];

    _body = new IndexedStack(
      children: <Widget>[
        new NewsListPage(),
        new TweetListPage(),
        new Text(_appBarTitles[2]),
        new OscMyInfoPage(),
      ],
      index: _tabIndex,
    );
  }

  TextStyle getTabTextStyle(int curIndex) {
    if (curIndex == _tabIndex) {
      return _tabTextStyleSelected;
    }
    return _tabTextStyleNormal;
  }

  Image getTabIcon(int curIndex) {
    return _tabImages[curIndex][curIndex == _tabIndex ? 1 : 0];
  }

  Text getTabTitle(int curIndex) {
    return new Text(_appBarTitles[curIndex], style: getTabTextStyle(curIndex));
  }

  @override
  Widget build(BuildContext context) {
    initData();
    return new MaterialApp(
      theme: new ThemeData(primaryColor: AppColors.colorPrimary),
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text(_appBarTitles[_tabIndex],
              style: new TextStyle(color: Colors.white)),
          iconTheme: new IconThemeData(color: Colors.white),
        ),
        body: _body,
        bottomNavigationBar: new CupertinoTabBar(
          items: <BottomNavigationBarItem>[
            new BottomNavigationBarItem(
                icon: getTabIcon(0), title: getTabTitle(0)),
            new BottomNavigationBarItem(
                icon: getTabIcon(1), title: getTabTitle(1)),
            new BottomNavigationBarItem(
                icon: getTabIcon(2), title: getTabTitle(2)),
            new BottomNavigationBarItem(
                icon: getTabIcon(3), title: getTabTitle(3)),
          ].map((BottomNavigationBarItem item) => item).toList(),
          currentIndex: _tabIndex,
          onTap: (index) {
            setState(() {
              _tabIndex = index;
            });
          },
        ),
        drawer: new MyDrawer(),
      ),
    );
  }
}

class MyDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Text("AAAA");
  }
}
