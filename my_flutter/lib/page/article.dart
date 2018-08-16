import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/item/Item.dart';
import 'package:my_flutter/widgets/widget.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

class ArticleDetailPage extends StatefulWidget {
  final title;
  final url;

  ArticleDetailPage({Key key, @required this.title, @required this.url})
      : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return new ArticleDetailPageState();
  }
}

class ArticleDetailPageState extends State<ArticleDetailPage> {
  bool isLoading = true;
  final flutterWebviewPlugin = new FlutterWebviewPlugin();

  @override
  void initState() {
    super.initState();
    flutterWebviewPlugin.onStateChanged.listen((state) {
      debugPrint('state:_' + state.type.toString());
      if (state.type == WebViewState.finishLoad) {
        setState(() {
          isLoading = false;
        });
      } else if (state.type == WebViewState.startLoad) {
        setState(() {
          isLoading = true;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    print(widget.url);
    print(widget.title);
    return new WebviewScaffold(
      url: widget.url,
      appBar: new AppBar(
        title: new Text(widget.title),
        bottom: new PreferredSize(
            child: isLoading
                ? new LinearProgressIndicator()
                : new Divider(
                    height: 1.0,
                    color: Theme.of(context).accentColor,
                  ),
            preferredSize: const Size.fromHeight(1.0)),
      ),
      withZoom: false,
      withJavascript: true,
      withLocalStorage: true,
    );
  }
}

class ArticlesPage extends StatefulWidget {
  final _itemData;

  ArticlesPage(this._itemData);

  @override
  State<StatefulWidget> createState() {
    return new ArticlesPageState();
  }
}

class ArticlesPageState extends State<ArticlesPage>
    with SingleTickerProviderStateMixin {
  TabController _tabController;
  List<Tab> _tabs = new List();
  List<dynamic> _list;

  @override
  void initState() {
    _list = widget._itemData['children'];
    for (var value in _list) {
      _tabs.add(new Tab(
        text: value['name'],
      ));
    }
    _tabController = new TabController(length: _list.length, vsync: this);
    super.initState();
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget._itemData['name']),
      ),
      body: new DefaultTabController(
          length: _list.length,
          child: new Scaffold(
            appBar: new TabBar(
              tabs: _tabs,
              isScrollable: true,
              controller: _tabController,
              labelColor: Theme.of(context).accentColor,
              unselectedLabelColor: Colors.black,
              indicatorColor: Theme.of(context).accentColor,
            ),
            body: new TabBarView(
              children: _list.map((dynamic itemData) {
                return new ArticleListPage(itemData['id'].toString());
              }).toList(),
              controller: _tabController,
            ),
          )),
    );
  }
}

class ArticleListPage extends StatefulWidget {
  final _id;

  ArticleListPage(this._id);

  @override
  State<StatefulWidget> createState() {
    return new ArticleListPageState();
  }
}

class ArticleListPageState extends State<ArticleListPage> {
  List _listData = new List();
  var _curPage = 0;
  var _listTotalSize = 0;
  ScrollController _scrollController = new ScrollController();

  @override
  void initState() {
    super.initState();
    _getArticleList();
    _scrollController.addListener(() {
      var maxScroll = _scrollController.position.maxScrollExtent;
      var pixels = _scrollController.position.pixels;
      if (maxScroll == pixels && _listData.length < _listTotalSize) {
        _getArticleList();
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
            _getArticleList();
          });
    }
  }

  void _getArticleList() {
    Map<String, String> params = new Map();
    params['cid'] = widget._id;
    HttpUtil.get(Api.ARTICLE_LIST + "$_curPage/json", (data) {
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
    return new ArticleItem(itemData, false, null);
  }
}
