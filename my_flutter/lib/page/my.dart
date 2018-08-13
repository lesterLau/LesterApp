import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/article.dart';
import 'package:my_flutter/page/login.dart';
import 'package:my_flutter/utils/utils.dart';
import 'package:my_flutter/widgets/widget.dart';

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
          await DataUtils.login().then((isLogin) {
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
        await DataUtils.login().then((isLogin) {
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
             Icons.favorite ,
            color:Colors.red ,
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
    HttpUtil.post(Api.UNCOLLECT_LIST + itemData['id'].toString() + "/json", (data) {
      setState(() {
        _listData.remove(itemData);
      });
    }, params: params);
  }
}
