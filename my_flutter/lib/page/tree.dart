import 'package:flutter/material.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/article.dart';

class TreePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new TreePageState();
  }
}

class TreePageState extends State<TreePage> {
  var _listData;

  @override
  void initState() {
    _getTree();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    if (_listData == null) {
      return new Center(
        child: new CircularProgressIndicator(),
      );
    } else {
      return new ListView.builder(
        itemBuilder: (c, i) => buildItem(i),
        itemCount: _listData.length,
      );
    }
  }

  void _getTree() {
    HttpUtil.get(Api.TREE, (data) {
      setState(() {
        _listData = data;
      });
    });
  }

  buildItem(int i) {
    var itemData = _listData[i];
    Widget name = new Text(
      itemData['name'],
      softWrap: true,
      style: new TextStyle(
          fontSize: 16.0, color: Colors.black, fontWeight: FontWeight.bold),
      textAlign: TextAlign.left,
    );
    List list = itemData['children'];
    String contentStr = '';
    for (var value in list) {
      contentStr += '${value['name']}';
    }
    Widget content = new Text(
      contentStr,
      style: new TextStyle(color: Colors.black),
      softWrap: true,
      textAlign: TextAlign.left,
    );
    return new Card(
      elevation: 4.0,
      child: new InkWell(
        onTap: () {
          _handleItemClick(itemData);
        },
        child: new Container(
          padding: EdgeInsets.all(15.0),
          child: new Row(
            children: <Widget>[
              new Expanded(
                  child: new Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  new Container(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: name,
                  ),
                  content
                ],
              )),
              new Icon(
                Icons.chevron_right,
                color: Colors.black,
              )
            ],
          ),
        ),
      ),
    );
  }

  void _handleItemClick(itemData) {
    Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
      return new ArticlesPage(itemData);
    }));
  }
}
