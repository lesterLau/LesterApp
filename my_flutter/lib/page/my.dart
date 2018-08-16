import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/article.dart';
import 'package:my_flutter/page/login.dart';
import 'package:my_flutter/utils/utils.dart';
import 'package:my_flutter/widgets/widget.dart';
import 'package:shared_preferences/shared_preferences.dart';

class MyInfoPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new MyInfoPageState();
  }
}

class MyInfoPageState extends State<MyInfoPage> {
  String _userName;

  @override
  void initState() {
    super.initState();
    _getName();
    Constants.eventBus.on(LoginEvent).listen((event) {
      _getName();
    });
  }

  @override
  Widget build(BuildContext context) {
    Widget image = new Image.asset(
      'images/ic_launcher_round.png',
      width: 100.0,
      height: 100.0,
    );
    Widget raiseButton = new RaisedButton(
        child: new Text(
          _userName == null ? "请登陆" : _userName,
          style: new TextStyle(color: Colors.white),
        ),
        color: Theme.of(context).accentColor,
        onPressed: () async {
          await DataUtils.isLogin().then((isLogin) {
            if (!isLogin) {
              Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
                return new LoginPage();
              }));
            }
          });
        });

    Widget listLike = new ListTile(
      leading: const Icon(Icons.favorite),
      title: const Text("喜欢的文章"),
      trailing: Icon(
        Icons.chevron_right,
        color: Theme.of(context).accentColor,
      ),
      onTap: () async {
        await DataUtils.isLogin().then((isLogin) {
          Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
            return isLogin ? new CollectPage() : new LoginPage();
          }));
        });
      },
    );

    Widget listAbout = new ListTile(
      leading: const Icon(Icons.info),
      title: const Text("关于我们"),
      trailing: Icon(
        Icons.chevron_right,
        color: Theme.of(context).accentColor,
      ),
    );
    Widget listLogout = new ListTile(
      leading: Icon(Icons.info),
      title: const Text("退出登陆"),
      trailing: Icon(
        Icons.chevron_right,
        color: Theme.of(context).accentColor,
      ),
      onTap: () async {
        DataUtils.clearLoginInfo();
        setState(() {
          _userName = null;
        });
      },
    );
    return new ListView(
      padding: const EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 0.0),
      children: <Widget>[image, raiseButton, listLike, listAbout, listLogout],
    );
  }

  void _getName() {
    DataUtils.getUserName().then((userName) {
      setState(() {
        _userName = userName;
      });
    });
  }
}

class CollectPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("喜欢的文章"),
      ),
      body: new CollectListPage(),
    );
  }
}

class CollectListPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new CollectListPageState();
  }
}

class CollectListPageState extends State<CollectListPage> {
  List _listData = new List();
  var _curPage = 0;
  var _listTotalSize = 0;
  ScrollController _scrollController = new ScrollController();

  @override
  void initState() {
    super.initState();
    _getCollectList();
    _scrollController.addListener(() {
      var maxScroll = _scrollController.position.maxScrollExtent;
      var pixels = _scrollController.position.pixels;
      if (maxScroll == pixels && _listData.length < _listTotalSize) {
        _getCollectList();
      }
    });
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (_listData == null || _listData.isEmpty) {
      return new Center(
        child: new CircularProgressIndicator(),
      );
    } else {
      return new RefreshIndicator(
          child: new ListView.builder(
              physics: new AlwaysScrollableScrollPhysics(),
              itemCount: _listData.length,
              controller: _scrollController,
              itemBuilder: (c, i) => buildItem(i)),
          onRefresh: () async {
            _curPage = 0;
            _getCollectList();
          });
    }
  }

  void _getCollectList() {
    HttpUtil.get(Api.COLLECT_LIST + "$_curPage/json", (data) {
      if (data != null) {
        Map<String, dynamic> dataMap = data;
        var _newListData = dataMap['datas'];
        _listTotalSize = dataMap["total"];
        setState(() {
          List _list = new List();
          if (_curPage == 0) {
            _listData.clear();
          }
          _curPage++;
          _list.addAll(_listData);
          _list.addAll(_newListData);
          if (_list.length >= _listTotalSize) {
            _list.add(Constants.endLineTag);
          }
          _listData = _list;
        });
      }
    });
  }

  Widget buildItem(int i) {
    var itemData = _listData[i];
    if (i == _listData.length - 1 &&
        itemData.toString() == Constants.endLineTag) {
      return new EndLine();
    }
    Row row1 = new Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        new Expanded(
            child: new Row(
          children: <Widget>[
            new Text("作者："),
            new Text(
              itemData['author'],
              style: new TextStyle(color: Theme.of(context).accentColor),
            ),
          ],
        )),
        new Text(itemData['niceDate'])
      ],
    );
    Row row2 = new Row(
      children: <Widget>[
        new Expanded(
            child: new Text.rich(new TextSpan(text: itemData['title'])))
      ],
    );
    Row row3 = new Row(
      children: <Widget>[
        new Expanded(
            child: new Text(
          itemData['chapterName'],
          softWrap: true,
          style: new TextStyle(color: Theme.of(context).accentColor),
          textAlign: TextAlign.left,
        )),
        new GestureDetector(
          child: new Icon(
            Icons.favorite,
            color: Colors.red,
          ),
          onTap: () {
            _handleOnItemCollect(itemData);
          },
        )
      ],
    );
    Column column = new Column(
      children: <Widget>[
        new Padding(
          padding: EdgeInsets.all(10.0),
          child: row1,
        ),
        new Padding(
          padding: EdgeInsets.fromLTRB(10.0, 5.0, 10.0, 5.0),
          child: row2,
        ),
        new Padding(
          padding: EdgeInsets.fromLTRB(10.0, 5.0, 10.0, 5.0),
          child: row3,
        )
      ],
    );
    return new Card(
      elevation: 4.0,
      child: new InkWell(
        child: column,
        onTap: () {
          _itemClick(itemData);
        },
      ),
    );
  }

  void _itemClick(itemData) {
    Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
      return new ArticleDetailPage(
          title: itemData['title'], url: itemData['link']);
    }));
  }

  void _handleOnItemCollect(itemData) {
    Map<String, String> params = new Map();
    params['originId'] = itemData['originId'].toString();
    HttpUtil.post(Api.UNCOLLECT_LIST + itemData['id'].toString() + "/json",
        (data) {
      setState(() {
        _listData.remove(itemData);
      });
    }, params: params);
  }
}

class OscMyInfoPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new OscMyInfoPageState();
  }
}

class OscMyInfoPageState extends State<OscMyInfoPage> {
  static const double IMAGE_ICON_WIDTH = 30.0;
  static const double ARROW_ICON_WIDTH = 30.0;
  var titles = ['我的消息', '阅读记录', '我的博客', '我的问答', '我的活动', '我的团队', '邀请好友'];
  var imagePaths = [
    "images/ic_my_message.png",
    "images/ic_my_blog.png",
    "images/ic_my_blog.png",
    "images/ic_my_question.png",
    "images/ic_discover_pos.png",
    "images/ic_my_team.png",
    "images/ic_my_recommend.png"
  ];
  var icons = [];
  var userAvatar;
  var userName;
  var titleTextStyle = new TextStyle(fontSize: 16.0);
  var rightArrowIcon = new Image.asset(
    'images/ic_arrow_right.png',
    width: ARROW_ICON_WIDTH,
    height: ARROW_ICON_WIDTH,
  );

  OscMyInfoPageState() {
    for (int i = 0; i < imagePaths.length; i++) {
      icons.add(getIconImage(imagePaths[i]));
    }
  }

  Widget getIconImage(path) {
    return new Padding(
      padding: const EdgeInsets.fromLTRB(0.0, 0.0, 10.0, 0.0),
      child: new Image.asset(path,
          width: IMAGE_ICON_WIDTH, height: IMAGE_ICON_WIDTH),
    );
  }

  @override
  void initState() {
    super.initState();
    _showUserInfo();
    Constants.eventBus.on(LoginEvent).listen((event) {
      // 收到登录的消息，重新获取个人信息
      if (event is LoginEvent && !event.isLogin) {
        // 收到退出登录的消息，刷新个人信息显示
        _showUserInfo();
      } else {
        getUserInfo();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var listView = new ListView.builder(
      itemCount: titles.length * 2,
      itemBuilder: (context, i) => renderRow(i),
    );
    return listView;
  }

  _showUserInfo() {
    OSCDataUtils.getUserInfo().then((UserInfo userInfo) {
      if (userInfo != null) {
        print(userInfo.name);
        print(userInfo.avatar);
        setState(() {
          userAvatar = userInfo.avatar;
          userName = userInfo.name;
        });
      } else {
        setState(() {
          userAvatar = null;
          userName = null;
        });
      }
    });
  }

  getUserInfo() async {
    SharedPreferences sp = await SharedPreferences.getInstance();
    String accessToken = sp.get(OSCDataUtils.SP_AC_TOKEN);
    Map<String, String> params = new Map();
    params['access_token'] = accessToken;
    HttpUtil.get(Api.USER_INFO, (data) {
      if (data != null) {
        var map = json.decode(data);
        setState(() {
          userAvatar = map['avatar'];
          userName = map['name'];
        });
        OSCDataUtils.saveUserInfo(map);
      }
    }, params: params);
  }

  renderRow(i) {
    if (i == 0) {
      var avatarContainer = new Container(
        color: const Color(0xff63ca6c),
        height: 200.0,
        child: new Center(
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              userAvatar == null
                  ? new Image.asset(
                      "images/ic_avatar_default.png",
                      width: 60.0,
                    )
                  : new Container(
                      width: 60.0,
                      height: 60.0,
                      decoration: new BoxDecoration(
                        shape: BoxShape.circle,
                        color: Colors.transparent,
                        image: new DecorationImage(
                            image: new NetworkImage(userAvatar),
                            fit: BoxFit.cover),
                        border: new Border.all(
                          color: Colors.white,
                          width: 2.0,
                        ),
                      ),
                    ),
              new Text(
                userName == null ? "点击头像登录" : userName,
                style: new TextStyle(color: Colors.white, fontSize: 16.0),
              ),
            ],
          ),
        ),
      );
      return new GestureDetector(
        onTap: () {
          OSCDataUtils.isLogin().then((isLogin) {
            if (isLogin) {
              // 已登录，显示用户详细信息
              _showUserInfoDetail();
            } else {
              // 未登录，跳转到登录页面
              _login();
            }
          });
        },
        child: avatarContainer,
      );
    }
    --i;
    if (i.isOdd) {
      return new Divider(
        height: 1.0,
      );
    }
    i = i ~/ 2;
    String title = titles[i];
    var listItemContent = new Padding(
      padding: const EdgeInsets.fromLTRB(10.0, 15.0, 10.0, 15.0),
      child: new Row(
        children: <Widget>[
          icons[i],
          new Expanded(
              child: new Text(
            title,
            style: titleTextStyle,
          )),
          rightArrowIcon
        ],
      ),
    );
    return new InkWell(
      child: listItemContent,
      onTap: () {
        _handleListItemClick(title);
//        Navigator
//            .of(context)
//            .push(new MaterialPageRoute(builder: (context) => new CommonWebPage(title: "Test", url: "https://my.oschina.net/u/815261/blog")));
      },
    );
  }

  void _showUserInfoDetail() {}

  _login() {
    Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new OSCLoginPage();
    }));
  }

  _handleListItemClick(String title) {
    OSCDataUtils.isLogin().then((isLogin) {
      if (!isLogin) {
        // 未登录
        _showLoginDialog();
      } else {
        OSCDataUtils.getUserInfo().then((info) {
          Navigator.of(context).push(new MaterialPageRoute(
              builder: (context) => new ArticleDetailPage(
                  title: "我的博客",
                  url: "https://my.oschina.net/u/${info.id}/blog")));
        });
      }
    });
  }
  _showLoginDialog() {
    showDialog(
        context: context,
        builder: (BuildContext ctx) {
          return new AlertDialog(
            title: new Text('提示'),
            content: new Text('没有登录，现在去登录吗？'),
            actions: <Widget>[
              new FlatButton(
                child: new Text(
                  '取消',
                  style: new TextStyle(color: Colors.red),
                ),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
              new FlatButton(
                child: new Text(
                  '确定',
                  style: new TextStyle(color: Colors.blue),
                ),
                onPressed: () {
                  Navigator.of(context).pop();
                  _login();
                },
              )
            ],
          );
        });
  }

}
