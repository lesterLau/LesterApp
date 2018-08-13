import 'package:flutter/material.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/article.dart';
import 'package:my_flutter/page/login.dart';
import 'package:my_flutter/utils/utils.dart';

class ArticleItem extends StatefulWidget {
  final itemData;
  final bool fromSearch;
  final String id;

  ArticleItem(this.itemData, this.fromSearch, this.id);

  @override
  State<StatefulWidget> createState() {
    return new ArticleItemState();
  }
}

class ArticleItemState extends State<ArticleItem> {
  @override
  Widget build(BuildContext context) {
    bool isCollect = widget.itemData["collect"];

    Row row1 = new Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        new Expanded(
            child: new Row(
          children: <Widget>[
            new Text("作者："),
            new Text(
              widget.itemData['author'],
              style: new TextStyle(color: Theme.of(context).accentColor),
            ),
          ],
        )),
        new Text(widget.itemData['niceDate'])
      ],
    );
    Row row2 = new Row(
      children: <Widget>[
        new Expanded(
            child: new Text.rich(new TextSpan(text: widget.itemData['title'])))
      ],
    );
    Row row3 = new Row(
      children: <Widget>[
        new Expanded(
            child: new Text(
          widget.fromSearch ? "" : widget.itemData['chapterName'],
          softWrap: true,
          style: new TextStyle(color: Theme.of(context).accentColor),
          textAlign: TextAlign.left,
        )),
        new GestureDetector(
          child: new Icon(
            isCollect ? Icons.favorite : Icons.favorite_border,
            color: isCollect ? Colors.red : null,
          ),
          onTap: () {
            _handleOnItemCollect(widget.itemData);
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
          _itemClick(widget.itemData);
        },
      ),
    );
  }

  void _itemClick(itemData) async {
    await Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new ArticleDetailPage(title: itemData['title'], url: itemData['link']);
    }));
  }

  void _handleOnItemCollect(itemData) {
    DataUtils.login().then((isLogin) {
      if (isLogin) {
        _itemCollect(itemData);
      } else {
        _login();
      }
    });
  }

  void _itemCollect(itemData) {
    String url;
    if (itemData['collect']) {
      url = Api.UNCOLLECT_ORIGINID;
    } else {
      url = Api.COLLECT;
    }
    url += '${itemData["id"]}/json';
    HttpUtil.post(url, (data) {
      setState(() {
        itemData['collect'] = !itemData['collect'];
      });
    });
  }

  void _login() {
    Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new LoginPage();
    }));
  }
}
