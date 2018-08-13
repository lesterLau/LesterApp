import 'package:flutter/material.dart';
import 'package:my_flutter/page/article.dart';

class EndLine extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Container(
      color: const Color(0xffeeeeee),
      padding: const EdgeInsets.fromLTRB(5.0, 15.0, 5.0, 15.0),
      child: new Row(
        children: <Widget>[
          new Expanded(
              child: new Divider(
                height: 10.0,
              )),
          new Text(
            "我是有底线的",
            style: new TextStyle(color: Theme
                .of(context)
                .accentColor),
          ),
          new Expanded(
            child: new Divider(
              height: 10.0,
            ),
            flex: 1,
          )
        ],
      ),
    );
  }
}

class SlideView extends StatefulWidget {
  final _data;

  SlideView(this._data);

  @override
  State<StatefulWidget> createState() {
    return new SlideViewState(_data);
  }
}

class SlideViewState extends State<SlideView>
    with SingleTickerProviderStateMixin {
  TabController _tabController;
  List _data;

  SlideViewState(this._data);

  @override
  void initState() {
    super.initState();
    _tabController = new TabController(
        length: _data == null ? 0 : _data.length, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> items = [];
    if (_data != null && _data.length > 0) {
      for (var i = 0; i < _data.length; i++) {
        var item = _data[i];
        var imageUrl = item['imagePath'];
        var title = item['title'];
        item['link'] = item['url'];
        items.add(new GestureDetector(
          onTap: () {
            _handOnItemClick(item);
          },
          child: AspectRatio(
            aspectRatio: 2.0 / 1.0,
            child: new Stack(
              children: <Widget>[
                new Image.network(
                  imageUrl,
                  fit: BoxFit.cover,
                  width: 1000.0,
                ),
                new Align(
                  alignment: FractionalOffset.bottomCenter,
                  child: new Container(
                    width: 1000.0,
                    color: const Color(0x50000000),
                    padding: const EdgeInsets.all(5.0),
                    child: new Text(
                      title,
                      style: new TextStyle(color: Colors.white, fontSize: 15.0),
                    ),
                  ),
                )
              ],
            ),
          ),
        ));
      }
    }
    return new TabBarView(
      children: items,
      controller: _tabController,
    );
  }

  _handOnItemClick(item) {
    Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new ArticleDetailPage(title: item['title'], url: item['link']);
    }));
  }
}
