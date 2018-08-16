import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/article.dart';
import 'package:my_flutter/utils/utils.dart';
import 'package:my_flutter/widgets/widget.dart';

class NewsListPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new NewsListPageState();
  }
}

class NewsListPageState extends State<NewsListPage> {
  var _listData;
  var _slideData;
  var _curPage = 1;
  var _listTotalSize = 0;
  ScrollController _controller = new ScrollController();
  TextStyle _titleTextStyle = new TextStyle(fontSize: 15.0);
  TextStyle _subTitleTextStyle =
      new TextStyle(fontSize: 12.0, color: Colors.grey);

  NewsListPageState() {
    _controller.addListener(() {
      var _maxScroll = _controller.position.maxScrollExtent;
      var _pixels = _controller.position.pixels;
      if (_maxScroll == _pixels && _listData.length < _listTotalSize) {
        _curPage++;
        getNewList();
      }
    });
  }

  @override
  void initState() {
    super.initState();
    getNewList();
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
            itemBuilder: (c, i) => buildItem(i),
            itemCount: _listData.length * 2,
            controller: _controller,
          ),
          onRefresh: () async {
            _curPage = 1;
            getNewList();
          });
    }
  }

  void getNewList() {
    String url = Api.NEWS_LIST + "?pageIndex=$_curPage&pageSize=10";
    HttpUtil.get(url, (res) {
      if (res != null) {
        print(StringUtils.getStringBuffer("res===>", obj1: res));
        Map<String, dynamic> map = json.decode(res);
        print(
            StringUtils.getStringBuffer("map['code']===>", obj1: map['code']));
        print(StringUtils.getStringBuffer("map['msg']===>", obj1: map['msg']));
        if (map['code'] != 0) {
          return;
        }
        var data = map['msg'];
        _listTotalSize = data['news']['total'];
        print("_listTotalSize===");
        print(_listTotalSize);
        var _newListData = data['news']['data'];
        print(_newListData);
        var _newSlideData = data['slide'];
        print(_newSlideData);
        setState(() {
          if (_curPage == 1) {
            _listData = _newListData;
          } else {
            var cacheList = new List();
            cacheList.addAll(_listData);
            cacheList.addAll(_newListData);
            _listData = cacheList;
          }
          if (_listData.length > _listTotalSize) {
            _listData.add(Constants.endLineTag);
          }
          _slideData = _newSlideData;
        });
      }
    });
  }

  buildItem(int i) {
    if (i == 0) {
      return new Container(
        height: 180.0,
        child: new SlideView(_slideData),
      );
    }
    i -= 1;
    if (i.isOdd) {
      return new Divider(
        height: 1.0,
      );
    }
    i = i ~/ 2;
    var _itemData = _listData[i];
    if (_itemData is String && _itemData == Constants.endLineTag) {
      return new EndLine();
    }
    var titleRow = new Row(
      children: <Widget>[
        new Expanded(
            child: new Text(
          _itemData['title'],
          style: _titleTextStyle,
        ))
      ],
    );

    var timeRow = new Row(
      children: <Widget>[
        new Container(
          width: 20.0,
          height: 20.0,
          decoration: new BoxDecoration(
              shape: BoxShape.circle,
              color: AppColors.deviderBg,
              image: new DecorationImage(
                  image: new NetworkImage(_itemData['authorImg']),
                  fit: BoxFit.cover),
              border: new Border.all(color: AppColors.deviderBg, width: 2.0)),
        ),
        new Padding(
          padding: const EdgeInsets.all(0.0),
          child: new Text(
            _itemData['timeStr'],
            style: _subTitleTextStyle,
          ),
        ),
        new Expanded(
          child: new Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              new Text(
                "${_itemData['commCount']}",
                style: _subTitleTextStyle,
              ),
              new Image.asset('./images/ic_comment.png',
                  width: 16.0, height: 16.0),
            ],
          ),
          flex: 1,
        )
      ],
    );
    var thumbImgUrl = _itemData['thumb'];
    var thumbImg = new Container(
      margin: const EdgeInsets.all(10.0),
      width: 60.0,
      height: 60.0,
      decoration: new BoxDecoration(
          shape: BoxShape.circle,
          color: AppColors.deviderBg,
          image: new DecorationImage(
              image: thumbImgUrl != null && thumbImgUrl.length > 0
                  ? new NetworkImage(thumbImgUrl)
                  : new ExactAssetImage('./images/ic_img_default.jpg'),
              fit: BoxFit.cover),
          border: new Border.all(color: AppColors.deviderBg, width: 2.0)),
    );
    var row = new Row(
      children: <Widget>[
        new Expanded(
          child: new Padding(
            padding: const EdgeInsets.all(10.0),
            child: new Column(
              children: <Widget>[
                titleRow,
                new Padding(
                    child: timeRow,
                    padding: const EdgeInsets.fromLTRB(0.0, 8.0, 0.0, 0.0))
              ],
            ),
          ),
          flex: 1,
        ),
        new Padding(
          padding: const EdgeInsets.all(6.0),
          child: new Container(
            width: 100.0,
            height: 80.0,
            child: new Center(
              child: thumbImg,
            ),
            color: AppColors.deviderBg,
          ),
        )
      ],
    );
    return new InkWell(
      child: row,
      onTap: () {
        Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
          return new ArticleDetailPage(
              title: _itemData['title'], url: _itemData['detailUrl']);
        }));
      },
    );
  }
}
