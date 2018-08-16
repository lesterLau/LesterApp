import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:my_flutter/constant/Constants.dart';
import 'package:my_flutter/http/Http.dart';
import 'package:my_flutter/page/login.dart';
import 'package:my_flutter/utils/utils.dart';
import 'package:my_flutter/widgets/widget.dart';

class TweetListPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new TweetListPageState();
  }
}

class TweetListPageState extends State<TweetListPage> {
  List _hotTweetsList;
  List _normalTweetsList;
  TextStyle _authorTextStyle;
  TextStyle _subTitleStyle;
  RegExp _regExp1 = new RegExp("</.*>");
  RegExp _regExp2 = new RegExp("<.*>");
  int _hotCurPage = 1;
  int _normalCurPage = 1;
  bool _loading = false;
  ScrollController _hotController;
  ScrollController _normalController;
  bool _isLogin = false;

  TweetListPageState() {
    _authorTextStyle =
        new TextStyle(fontSize: 15.0, fontWeight: FontWeight.bold);
    _subTitleStyle = new TextStyle(fontSize: 12.0, color: Colors.grey);
    _normalController = new ScrollController();
    _normalController.addListener(() {
      var maxScroll = _normalController.position.maxScrollExtent;
      var pixels = _normalController.position.pixels;
      if (maxScroll == pixels) {
        // load next page
        _normalCurPage++;
        _getTweetsList(false);
      }
    });
    _hotController = new ScrollController();
    _hotController.addListener(() {
      var maxScroll = _hotController.position.maxScrollExtent;
      var pixels = _hotController.position.pixels;
      if (maxScroll == pixels) {
        // load next page
        _hotCurPage++;
        _getTweetsList(true);
      }
    });
  }

  @override
  void initState() {
    OSCDataUtils.isLogin().then((isLogin) {
      setState(() {
        _isLogin = isLogin;
      });
    });
    Constants.eventBus.on(LoginEvent).listen((event) {
      print("接收到登陆成功的消息");
      print(event);
      setState(() {
        if (event is LoginEvent) {
          print("====>LoginEvent");
          print(event.isLogin);
          _isLogin = event.isLogin;
        }
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    if (!_isLogin) {
      return new Center(
          child: new Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          new Container(
            padding: const EdgeInsets.all(10.0),
            child: new Center(
              child: new Column(
                children: <Widget>[
                  new Text("由于OSC的openapi限制"),
                  new Text("必须登陆后才能获取动弹信息")
                ],
              ),
            ),
          ),
          new InkWell(
            child: new Container(
              padding: const EdgeInsets.fromLTRB(15.0, 8.0, 15.0, 8.0),
              child: new Text("去登陆"),
              decoration: new BoxDecoration(
                  border: new Border.all(color: Colors.black),
                  borderRadius: new BorderRadius.all(new Radius.circular(5.0))),
            ),
            onTap: () {
              Navigator.of(context).push(new MaterialPageRoute(builder: (c) {
                return new OSCLoginPage();
              }));
            },
          )
        ],
      ));
    } else {
      return new DefaultTabController(
          length: 2,
          child: new Scaffold(
            appBar: new TabBar(tabs: <Widget>[
              new Tab(
                child: new Text(
                  '动弹列表',
                  style: new TextStyle(color: Colors.black),
                ),
              ),
              new Tab(
                child: new Text(
                  '热门动弹',
                  style: new TextStyle(color: Colors.black),
                ),
              )
            ]),
            body: new TabBarView(children: <Widget>[getNormal(), getHot()]),
          ));
    }
  }

  void _getTweetsList(bool isHot) {
    OSCDataUtils.isLogin().then((isLogin) {
      if (isLogin) {
        OSCDataUtils.getAccessToken().then((token) {
          print(StringUtils.getStringBuffer("token==>", obj1: token));
          if (token == null || token.length == 0) {
            return;
          }
          _loading = true;
          Map<String, String> params = new Map();
          params['access_token'] = token;
          params['page'] = isHot ? "$_hotCurPage" : "$_normalCurPage";
          if (isHot) {
            params['user'] = "-1";
          } else {
            params['user'] = "0";
          }
          params['pageSize'] = "20";
          params['dataType'] = "json";
          HttpUtil.get(Api.TWEETS_LIST, (res) {
            if (res != null) {
              print(StringUtils.getStringBuffer("res===>", obj1: res));
              Map<String, dynamic> obj = json.decode(res);
              if (isHot && _hotCurPage == 1) {
                _hotTweetsList = obj['tweetlist'];
              } else if (!isHot && _normalCurPage == 1) {
                _normalTweetsList = obj['tweetlist'];
              } else {
                if (isHot) {
                  List _list = new List();
                  _list.addAll(_hotTweetsList);
                  _list.addAll(obj['tweetlist']);
                  _hotTweetsList = _list;
                } else {
                  List _list = new List();
                  _list.addAll(_normalTweetsList);
                  _list.addAll(obj['tweetlist']);
                  _normalTweetsList = _list;
                }
              }
              print(StringUtils.getStringBuffer("isHot==>",
                  obj2: isHot,
                  obj3: isHot ? "\n_hotCurPage===>" : "\n_normalCurPage===>",
                  obj4: isHot ? _hotCurPage : _normalCurPage,
                  obj5: isHot
                      ? "\n_hotTweetsList===>"
                      : "\n_normalTweetsList===>",
                  obj6: isHot ? _hotTweetsList : _normalTweetsList));
              filterList(isHot ? _hotTweetsList : _normalTweetsList, isHot);
            }
          }, params: params);
        });
      }
    });
  }

  void filterList(List list, bool isHot) {
    BlackListUtils.getBlackListIds().then((blackListIds) {
      List _newList = new List();
      if (blackListIds != null && blackListIds.isNotEmpty && list != null) {
        for (dynamic item in list) {
          int authorId = item['authorId'];
          if (!blackListIds.contains(authorId)) {
            _newList.add(item);
          }
        }
      } else {
        _newList = list;
      }
      setState(() {
        if (isHot) {
          _hotTweetsList = _newList;
        } else {
          _normalTweetsList = _newList;
        }
        _loading = false;
      });
    });
  }

  getNormal() {
    if (_normalTweetsList == null) {
      _normalCurPage = 1;
      _getTweetsList(false);
      return new Center(
        child: new CircularProgressIndicator(),
      );
    } else {
      return new RefreshIndicator(
          child: new ListView.builder(
            itemBuilder: (c, i) => buildNormalItem(i),
            itemCount: _normalTweetsList.length * 2 - 1,
            physics: const AlwaysScrollableScrollPhysics(),
            controller: _normalController,
          ),
          onRefresh: () async {
            _normalCurPage = 1;
            _getTweetsList(false);
          });
    }
  }

  getHot() {
    if (_hotTweetsList == null) {
      _hotCurPage = 1;
      _getTweetsList(true);
      return new Center(
        child: new CircularProgressIndicator(),
      );
    } else {
      return new RefreshIndicator(
          child: new ListView.builder(
            itemBuilder: (c, i) => buildHotItem(i),
            itemCount: _hotTweetsList.length * 2 - 1,
            physics: const AlwaysScrollableScrollPhysics(),
            controller: _hotController,
          ),
          onRefresh: () async {
            _hotCurPage = 1;
            _getTweetsList(true);
          });
    }
  }

  Widget buildNormalItem(int i) {
    if (i.isOdd) {
      return new Divider(
        height: 1.0,
      );
    } else {
      i = i ~/ 2;
      return getRowWidget(_normalTweetsList[i]);
    }
  }

  Widget buildHotItem(int i) {
    if (i.isOdd) {
      return new Divider(
        height: 1.0,
      );
    } else {
      i = i ~/ 2;
      return getRowWidget(_hotTweetsList[i]);
    }
  }

  Widget getRowWidget(item) {
    var authorRow = new Row(
      children: <Widget>[
        new Container(
          width: 35.0,
          height: 35.0,
          decoration: new BoxDecoration(
              image: new DecorationImage(
                  image: new NetworkImage(
                    item['portrait'],
                  ),
                  fit: BoxFit.cover),
              border: new Border.all(color: Colors.white, width: 2.0)),
        ),
        new Padding(
          padding: const EdgeInsets.only(left: 6.0),
          child: new Text(
            item['author'],
            style: _authorTextStyle,
          ),
        ),
        new Expanded(
            child: new Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            new Text(
              '${item['commentCount']}',
              style: _subTitleStyle,
            ),
            new Image.asset(
              './images/ic_comment.png',
              width: 16.0,
              height: 16.0,
            )
          ],
        ))
      ],
    );
    var _body = item['body'];
    _body = clearHtmlContent(_body);
    var contentRow = new Row(
      children: <Widget>[new Expanded(child: new Text(_body))],
    );
    var timeRow = new Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: <Widget>[
        new Text(
          item['pubDate'],
          style: _subTitleStyle,
        )
      ],
    );
    var columns = <Widget>[
      new Padding(
        padding: const EdgeInsets.fromLTRB(10.0, 10.0, 10.0, 2.0),
        child: authorRow,
      ),
      new Padding(
        padding: const EdgeInsets.fromLTRB(52.0, 0.0, 10.0, 0.0),
        child: contentRow,
      )
    ];
    String imageSmall = item['imgSmall'];
    if (imageSmall != null && imageSmall.length > 0) {
      List<String> list = imageSmall.split(',');
      List<String> imgUrlList = new List();
      for (String s in list) {
        if (s.startsWith('http')) {
          imgUrlList.add(s);
        } else {
          imgUrlList.add("https://static.oschina.net/uploads/space/" + s);
        }
      }
      List<Widget> imgList = [];
      List<List<Widget>> rows = [];
      num len = imgUrlList.length;
      for (var row = 0; row < getRow(len); row++) {
        List<Widget> rowArr = [];
        for (var col = 0; col < 3; col++) {
          num index = row * 3 + col;
          num screenWidth = MediaQuery.of(context).size.width;
          double cellWidth = (screenWidth - 100) / 3;
          if (index < len) {
            rowArr.add(new Padding(
              padding: const EdgeInsets.all(2.0),
              child: new Image.network(imgUrlList[index],
                  width: cellWidth, height: cellWidth),
            ));
          }
        }
        rows.add(rowArr);
      }
      for (var row in rows) {
        imgList.add(new Row(
          children: row,
        ));
      }
      columns.add(new Padding(
        padding: const EdgeInsets.fromLTRB(52.0, 5.0, 10.0, 0.0),
        child: new Column(
          children: imgList,
        ),
      ));
    }
    columns.add(new Padding(
      padding: const EdgeInsets.fromLTRB(0.0, 10.0, 10.0, 6.0),
      child: timeRow,
    ));
    return new InkWell(
      child: new Column(
        children: columns,
      ),
      onTap: () {
        // 跳转到动弹详情
        Navigator.of(context).push(new MaterialPageRoute(builder: (ctx) {
          return new TweetDetailPage(
            tweetData: item,
          );
        }));
      },
      onLongPress: () {
        showDialog(
            context: context,
            builder: (BuildContext ctx) {
              return new AlertDialog(
                title: new Text('提示'),
                content: new Text('要把\"${item['author']}\"关进小黑屋吗？'),
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
                      putIntoBlackHouse(item);
                    },
                  )
                ],
              );
            });
      },
    );
  }

  putIntoBlackHouse(item) {
    int authorId = item['authorid'];
    String portrait = "${item['portrait']}";
    String nickname = "${item['author']}";
    OSCDataUtils.getUserInfo().then((info) {
      print(info);
      if (info != null && info is UserInfo) {
        int loginUserId = info.id;
        Map<String, String> params = new Map();
        params['userid'] = '$loginUserId';
        params['authorid'] = '$authorId';
        params['authoravatar'] = portrait;
        params['authorname'] = Utf8Utils.encode(nickname);
        HttpUtil.post(Api.ADD_TO_BLACK, (data) {
          Navigator.of(context).pop();
          if (data != null) {
            print(data);
            var obj = json.decode(data);
            if (obj['code'] == 0) {
              // 添加到小黑屋成功
              showAddBlackHouseResultDialog("添加到小黑屋成功！");
              BlackListUtils.addBlackId(authorId).then((arg) {
                // 添加之后，重新过滤数据
                filterList(_normalTweetsList, false);
                filterList(_hotTweetsList, true);
              });
            } else {
              // 添加失败
              var msg = obj['msg'];
              showAddBlackHouseResultDialog("添加到小黑屋失败：$msg");
            }
          }
        }, params: params);
      }
    });
  }

  showAddBlackHouseResultDialog(String msg) {
    showDialog(
        context: context,
        builder: (BuildContext ctx) {
          return new AlertDialog(
            title: new Text('提示'),
            content: new Text(msg),
            actions: <Widget>[
              new FlatButton(
                child: new Text(
                  '确定',
                  style: new TextStyle(color: Colors.red),
                ),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              )
            ],
          );
        });
  }

  int getRow(int n) {
    int a = n % 3;
    int b = n ~/ 3;
    if (a != 0) {
      return b + 1;
    }
    return b;
  }

  String clearHtmlContent(str) {
    if (str.startsWith("<emoji")) {
      return "[emoji]";
    }
    var s = str.replaceAll(_regExp1, "");
    s = s.replaceAll(_regExp2, "");
    s = s.replaceAll("\n", "");
    return s;
  }
}

class TweetDetailPage extends StatefulWidget {
  final Map<String, dynamic> tweetData;

  TweetDetailPage({Key key, this.tweetData}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return TweetDetailPageState();
  }
}

class TweetDetailPageState extends State<TweetDetailPage> {
  List commentList;
  RegExp regExp1 = new RegExp("</.*>");
  RegExp regExp2 = new RegExp("<.*>");
  TextStyle subtitleStyle =
      new TextStyle(fontSize: 12.0, color: const Color(0xFFB5BDC0));
  TextStyle contentStyle = new TextStyle(fontSize: 15.0, color: Colors.black);
  num curPage = 1;
  ScrollController _controller = new ScrollController();
  TextEditingController _inputController = new TextEditingController();

// 获取动弹的回复
  getReply(bool isLoadMore) {
   OSCDataUtils.isLogin().then((isLogin) {
      if (isLogin) {
        OSCDataUtils.getAccessToken().then((token) {
          if (token == null || token.length == 0) {
            return;
          }
          Map<String, String> params = new Map();
          var id = widget.tweetData['id'];
          params['id'] = '$id';
          params['catalog'] = '3'; // 3是动弹评论
          params['access_token'] = token;
          params['page'] = '$curPage';
          params['pageSize'] = '20';
          params['dataType'] = 'json';
          HttpUtil.get(Api.COMMENT_LIST, (data) {
            print(StringUtils.getStringBuffer("评论列表==>", obj: data));
            setState(() {
              if (!isLoadMore) {
                commentList = json.decode(data)['commentList'];
                if (commentList == null) {
                  commentList = new List();
                }
              } else {
                // 加载更多数据
                List list = new List();
                list.addAll(commentList);
                list.addAll(json.decode(data)['commentList']);
                if (list.length >= widget.tweetData['commentCount']) {
                  list.add(Constants.endLineTag);
                }
                commentList = list;
              }
            });
          }, params: params);
        });
      }
    });
  }

  @override
  void initState() {
    super.initState();
    getReply(false);
    _controller.addListener(() {
      var max = _controller.position.maxScrollExtent;
      var pixels = _controller.position.pixels;
      if (max == pixels &&
          commentList.length < widget.tweetData['commentCount']) {
        // scroll to end, load next page
        curPage++;
        getReply(true);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var _body = commentList == null
        ? new Center(
            child: new CircularProgressIndicator(),
          )
        : new ListView.builder(
            itemCount: commentList.length == 0 ? 1 : commentList.length * 2,
            itemBuilder: renderListItem,
            controller: _controller,
          );
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("动弹详情", style: new TextStyle(color: Colors.white)),
          iconTheme: new IconThemeData(color: Colors.white),
          actions: <Widget>[
            new IconButton(
              icon: new Icon(Icons.send),
              onPressed: () {
                // 回复楼主
                showReplyBottomView(context, true);
              },
            )
          ],
        ),
        body: _body);
  }

  Widget renderListItem(BuildContext context, int i) {
    if (i == 0) {
      return getTweetView(widget.tweetData);
    }
    i -= 1;
    if (i.isOdd) {
      return new Divider(
        height: 1.0,
      );
    }
    i ~/= 2;
    return _renderCommentRow(context, i);
  }

  // 渲染评论列表
  _renderCommentRow(context, i) {
    var listItem = commentList[i];
    if (listItem is String && listItem == Constants.endLineTag) {
      return new EndLine();
    }
    String avatar = listItem['commentPortrait'];
    String author = listItem['commentAuthor'];
    String date = listItem['pubDate'];
    String content = listItem['content'];
    content = clearHtmlContent(content);
    var row = new Row(
      children: <Widget>[
        new Padding(
            padding: const EdgeInsets.all(10.0),
            child: new Image.network(
              avatar,
              width: 35.0,
              height: 35.0,
            )),
        new Expanded(
            child: new Container(
          margin: const EdgeInsets.fromLTRB(0.0, 5.0, 0.0, 5.0),
          child: new Column(
            children: <Widget>[
              new Row(
                children: <Widget>[
                  new Expanded(
                    child: new Text(
                      author,
                      style: new TextStyle(color: const Color(0xFF63CA6C)),
                    ),
                  ),
                  new Padding(
                      padding: const EdgeInsets.fromLTRB(0.0, 0.0, 10.0, 0.0),
                      child: new Text(
                        date,
                        style: subtitleStyle,
                      ))
                ],
              ),
              new Padding(
                  padding: const EdgeInsets.fromLTRB(0.0, 0.0, 10.0, 0.0),
                  child: new Row(
                    children: <Widget>[
                      new Expanded(
                          child: new Text(
                        content,
                        style: contentStyle,
                      ))
                    ],
                  ))
            ],
          ),
        ))
      ],
    );
    return new Builder(
      builder: (ctx) {
        return new InkWell(
          onTap: () {
            showReplyBottomView(ctx, false, data: listItem);
          },
          child: row,
        );
      },
    );
  }

  showReplyBottomView(ctx, bool isMainFloor, {data}) {
    String title;
    String authorId;
    if (isMainFloor) {
      title = "@${widget.tweetData['author']}";
      authorId = "${widget.tweetData['authorid']}";
    } else {
      title = "@${data['commentAuthor']}";
      authorId = "${data['commentAuthorId']}";
    }
    print("authorId = $authorId");
    showModalBottomSheet(
        context: ctx,
        builder: (sheetCtx) {
          return new Container(
              height: 230.0,
              padding: const EdgeInsets.all(20.0),
              child: new Column(
                children: <Widget>[
                  new Row(
                    children: <Widget>[
                      new Text(isMainFloor ? "回复楼主" : "回复"),
                      new Expanded(
                          child: new Text(
                        title,
                        style: new TextStyle(color: const Color(0xFF63CA6C)),
                      )),
                      new InkWell(
                        child: new Container(
                          padding:
                              const EdgeInsets.fromLTRB(10.0, 6.0, 10.0, 6.0),
                          decoration: new BoxDecoration(
                              border: new Border.all(
                                color: const Color(0xFF63CA6C),
                                width: 1.0,
                              ),
                              borderRadius: new BorderRadius.all(
                                  new Radius.circular(6.0))),
                          child: new Text(
                            "发送",
                            style:
                                new TextStyle(color: const Color(0xFF63CA6C)),
                          ),
                        ),
                        onTap: () {
                          // 发送回复
                          sendReply(authorId);
                        },
                      )
                    ],
                  ),
                  new Container(
                    height: 10.0,
                  ),
                  new TextField(
                    maxLines: 5,
                    controller: _inputController,
                    decoration: new InputDecoration(
                        hintText: "说点啥～",
                        hintStyle:
                            new TextStyle(color: const Color(0xFF808080)),
                        border: new OutlineInputBorder(
                          borderRadius: const BorderRadius.all(
                              const Radius.circular(10.0)),
                        )),
                  )
                ],
              ));
        });
  }

  void sendReply(authorId) {
    String replyStr = _inputController.text;
    if (replyStr == null ||
        replyStr.length == 0 ||
        replyStr.trim().length == 0) {
      return;
    } else {
      OSCDataUtils.isLogin().then((isLogin) {
        if (isLogin) {
          OSCDataUtils.getAccessToken().then((token) {
            Map<String, String> params = new Map();
            params['access_token'] = token;
            params['id'] = "${widget.tweetData['id']}";
            print("id: ${widget.tweetData['id']}");
            params['catalog'] = "3";
            params['content'] = replyStr;
            params['authorid'] = "$authorId";
            print("authorId: $authorId");
            params['isPostToMyZone'] = "0";
            params['dataType'] = "json";
            HttpUtil.get(Api.COMMENT_REPLY, (data) {
              if (data != null) {
                var obj = json.decode(data);
                var error = obj['error'];
                if (error != null && error == '200') {
                  // 回复成功
                  Navigator.of(context).pop();
                  getReply(false);
                }
              }
            }, params: params);
          });
        }
      });
    }
  }

  Widget getTweetView(Map<String, dynamic> listItem) {
    var authorRow = new Row(
      children: <Widget>[
        new Container(
          width: 35.0,
          height: 35.0,
          decoration: new BoxDecoration(
            shape: BoxShape.circle,
            color: Colors.blue,
            image: new DecorationImage(
                image: new NetworkImage(listItem['portrait']),
                fit: BoxFit.cover),
            border: new Border.all(
              color: Colors.white,
              width: 2.0,
            ),
          ),
        ),
        new Padding(
            padding: const EdgeInsets.fromLTRB(6.0, 0.0, 0.0, 0.0),
            child: new Text(listItem['author'],
                style: new TextStyle(
                  fontSize: 16.0,
                ))),
        new Expanded(
          child: new Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              new Text(
                '${listItem['commentCount']}',
                style: subtitleStyle,
              ),
              new Image.asset(
                './images/ic_comment.png',
                width: 20.0,
                height: 20.0,
              )
            ],
          ),
        )
      ],
    );
    var _body = listItem['body'];
    _body = clearHtmlContent(_body);
    var contentRow = new Row(
      children: <Widget>[
        new Expanded(
          child: new Text(_body),
        )
      ],
    );
    var timeRow = new Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        new Text(
          listItem['pubDate'],
          style: subtitleStyle,
        )
      ],
    );
    var columns = <Widget>[
      new Padding(
        padding: const EdgeInsets.fromLTRB(10.0, 10.0, 10.0, 2.0),
        child: authorRow,
      ),
      new Padding(
        padding: const EdgeInsets.fromLTRB(52.0, 0.0, 10.0, 0.0),
        child: contentRow,
      ),
    ];
    String imgSmall = listItem['imgSmall'];
    if (imgSmall != null && imgSmall.length > 0) {
      // 动弹中有图片
      List<String> list = imgSmall.split(",");
      List<String> imgUrlList = new List<String>();
      for (String s in list) {
        if (s.startsWith("http")) {
          imgUrlList.add(s);
        } else {
          imgUrlList.add("https://static.oschina.net/uploads/space/" + s);
        }
      }
      List<Widget> imgList = [];
      List rows = [];
      num len = imgUrlList.length;
      for (var row = 0; row < getRow(len); row++) {
        List<Widget> rowArr = [];
        for (var col = 0; col < 3; col++) {
          num index = row * 3 + col;
          num screenWidth = MediaQuery.of(context).size.width;
          double cellWidth = (screenWidth - 100) / 3;
          if (index < len) {
            rowArr.add(new Padding(
              padding: const EdgeInsets.all(2.0),
              child: new Image.network(imgUrlList[index],
                  width: cellWidth, height: cellWidth),
            ));
          }
        }
        rows.add(rowArr);
      }
      for (var row in rows) {
        imgList.add(new Row(
          children: row,
        ));
      }
      columns.add(new Padding(
        padding: const EdgeInsets.fromLTRB(52.0, 5.0, 10.0, 0.0),
        child: new Column(
          children: imgList,
        ),
      ));
    }
    columns.add(new Padding(
      padding: const EdgeInsets.fromLTRB(52.0, 10.0, 10.0, 6.0),
      child: timeRow,
    ));
    columns.add(new Divider(
      height: 5.0,
    ));
    columns.add(new Container(
      margin: const EdgeInsets.fromLTRB(0.0, 6.0, 0.0, 0.0),
      child: new Row(
        children: <Widget>[
          new Container(
            width: 4.0,
            height: 20.0,
            color: const Color(0xFF63CA6C),
          ),
          new Expanded(
            flex: 1,
            child: new Container(
                height: 20.0,
                color: const Color(0xFFECECEC),
                child: new Text(
                  "评论列表",
                  style: new TextStyle(color: const Color(0xFF63CA6C)),
                )),
          )
        ],
      ),
    ));
    return new Column(
      children: columns,
    );
  }

  int getRow(int n) {
    int a = n % 3;
    int b = n ~/ 3;
    if (a != 0) {
      return b + 1;
    }
    return b;
  }

  // 去掉文本中的html代码
  String clearHtmlContent(String str) {
    if (str.startsWith("<emoji")) {
      return "[emoji]";
    }
    var s = str.replaceAll(regExp1, "");
    s = s.replaceAll(regExp2, "");
    s = s.replaceAll("\n", "");
    return s;
  }
}
