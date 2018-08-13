import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/item/Item.dart';
import 'package:my_flutter/widgets/widget.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new HomePageState();
  }
}

class HomePageState extends State<HomePage> {
  List _listData = new List();
  var _bannerData;
  var _curPage = 0;
  var _listTotalSize = 0;
  SlideView _bannerView;
  ScrollController _scrollController = new ScrollController();

  HomePageState() {
    _scrollController.addListener(() {
      var _maxScrollExtent = _scrollController.position.maxScrollExtent;
      var _pixels = _scrollController.position.pixels;
      if (_maxScrollExtent == _pixels && _listData.length < _listTotalSize) {
        getHomeData();
      }
    });
  }

  @override
  void initState() {
    super.initState();
    getBannerData();
    getHomeData();
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
            itemBuilder: (context, i) => buildItem(i),
            itemCount: _listData.length + 1,
            controller: _scrollController,
          ),
          onRefresh: () async {
            _curPage = 0;
            getBannerData();
            getHomeData();
          });
    }
  }

  void getBannerData() {
    HttpUtil.get(Api.BANNER, (data) {
      if (data != null) {
        setState(() {
          _bannerData = data;
          _bannerView = new SlideView(_bannerData);
        });
      }
    });
  }

  void getHomeData() {
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
    });
  }

  Widget buildItem(int i) {
    if (i == 0) {
      return new Container(
        height: 180.0,
        child: _bannerView,
      );
    }
    i -= 1;
    var itemData = _listData[i];
    if (itemData is String && itemData == Constants.endLineTag) {
      return new EndLine();
    }
    return new ArticleItem(itemData, false, null);
  }
}
