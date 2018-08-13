import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/item/Item.dart';
import 'package:my_flutter/page/article.dart';
import 'package:my_flutter/widgets/widget.dart';

class SearchPage extends StatefulWidget {
  final String _searchStr;

  SearchPage(this._searchStr);

  @override
  State<StatefulWidget> createState() {
    return new SearchPageState();
  }
}

class SearchPageState extends State<SearchPage> {
  TextEditingController _editingController = new TextEditingController();
  SearchListPage _searchListPage;

  @override
  void initState() {
    super.initState();
    _editingController = new TextEditingController(text: widget._searchStr);
    changeContent();
  }

  void changeContent() {
    setState(() {
      _searchListPage = new SearchListPage(_editingController.text);
    });
  }

  @override
  Widget build(BuildContext context) {
    TextField textField = new TextField(
      autofocus: true,
      decoration:
          new InputDecoration(border: InputBorder.none, hintText: '搜索关键词'),
      controller: _editingController,
    );
    return new Scaffold(
      appBar: new AppBar(
        title: textField,
        actions: <Widget>[
          new IconButton(
              icon: new Icon(Icons.search),
              onPressed: () {
                changeContent();
              }),
          new IconButton(
              icon: new Icon(Icons.close),
              onPressed: () {
                setState(() {
                  _editingController.clear();
                });
              })
        ],
      ),
      body: (_editingController.text == null || _editingController.text.isEmpty)
          ? new Center(
              child: new HotPage(),
            )
          : _searchListPage,
    );
  }
}

class HotPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new HotPageState();
  }
}

class HotPageState extends State<HotPage> {
  List<Widget> _hotKeyWidgets = new List();
  List<Widget> _friendsWidgets = new List();

  @override
  void initState() {
    super.initState();
    _getHotKey();
    _getFriend();
  }

  @override
  Widget build(BuildContext context) {
    return new ListView(
      children: <Widget>[
        new Padding(
          padding: EdgeInsets.all(10.0),
          child: new Text(
            '大家都在搜',
            style: new TextStyle(
                color: Theme.of(context).accentColor, fontSize: 20.0),
          ),
        ),
        new Wrap(
          spacing: 5.0,
          runSpacing: 5.0,
          children: _hotKeyWidgets,
        ),
        new Padding(
          padding: EdgeInsets.all(10.0),
          child: new Text(
            '常用网站',
            style: new TextStyle(
                color: Theme.of(context).accentColor, fontSize: 20.0),
          ),
        ),
        new Wrap(
          spacing: 5.0,
          runSpacing: 5.0,
          children: _friendsWidgets,
        )
      ],
    );
  }

  void _getHotKey() {
    HttpUtil.get(Api.HOTKEY, (data) {
      if (data != null) {
        setState(() {
          List _datas = data;
          _hotKeyWidgets.clear();
          for (var value in _datas) {
            Widget widget = new ActionChip(
                backgroundColor: Theme.of(context).accentColor,
                label: new Text(
                  value['name'],
                  style: new TextStyle(color: Colors.white),
                ),
                onPressed: () {
                  Navigator
                      .of(context)
                      .push(new MaterialPageRoute(builder: (c) {
                    return new SearchPage(value['name']);
                  }));
                });
            _hotKeyWidgets.add(widget);
          }
        });
      }
    });
  }

  void _getFriend() {
    HttpUtil.get(Api.FRIEND, (data) {
      if (data != null) {
        List _datas = data;
        _friendsWidgets.clear();
        for (var item in _datas) {
          Widget widget = new ActionChip(
              backgroundColor: Theme.of(context).accentColor,
              label: new Text(
                item['name'],
                style: new TextStyle(color: Colors.white),
              ),
              onPressed: () {
                Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
                  return new ArticleDetailPage(
                      title: item['name'], url: item['link']);
                }));
              });
          _friendsWidgets.add(widget);
        }
      }
    });
  }
}

class SearchListPage extends StatefulWidget {
  final String _id;

  SearchListPage(this._id);

  @override
  State<StatefulWidget> createState() {
    return new SearchListPageState();
  }
}

class SearchListPageState extends State<SearchListPage> {
  List _listData = new List();
  var _curPage = 0;
  var _listTotalSize = 0;
  ScrollController _scrollController = new ScrollController();

  @override
  void initState() {
    super.initState();
    _scrollController.addListener(() {
      var _maxScrollExtent = _scrollController.position.maxScrollExtent;
      var _pixels = _scrollController.position.pixels;
      if (_maxScrollExtent == _pixels && _listData.length < _listTotalSize) {
        _articleQuery();
      }
    });
    _articleQuery();
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
            itemBuilder: (context, i) => buildItem(i),
            itemCount: _listData.length,
            controller: _scrollController,
          ),
          onRefresh: () async {
            _curPage = 0;
            _articleQuery();
          });
    }
  }

  void _articleQuery() {
    Map<String, String> params = new Map();
    params['k'] = widget._id;
    HttpUtil.post(Api.ARTICLE_QUERY + "$_curPage/json", (data) {
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
    }, params: params);
  }

  Widget buildItem(int i) {
    var itemData = _listData[i];
    if (i == _listData.length - 1 &&
        itemData is String &&
        itemData == Constants.endLineTag) {
      return new EndLine();
    }
    return new ArticleItem(itemData, true, widget._id);
  }
}
