package com.lester.lesterapp.observe;

import com.blankj.utilcode.util.LogUtils;
import com.lesterlau.http.RxBus;

import java.util.concurrent.ConcurrentHashMap;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 消息处理中枢
 *
 * @Author lester
 * @Date 2018/7/24
 */
public class MsgMgr {

    private static MsgMgr instance;
    private CompositeSubscription mCompositeSubscription;
    private ConcurrentHashMap<PObserver, Subscription> observerDisposableMap;
    private ConcurrentHashMap<Class, PObserver> classObserverMap;

    private MsgMgr() {
        mCompositeSubscription = new CompositeSubscription();
        observerDisposableMap = new ConcurrentHashMap<>();
        classObserverMap = new ConcurrentHashMap<>();
    }

    public static MsgMgr getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new MsgMgr();
                }
            }
        }
        return instance;
    }

    /**
     * 抛出事件到主线程
     *
     * @param key   事件的key
     * @param value 事件的value
     */
    public void sendMsg(String key, Object value) {
        Msg msg = new Msg(key, value);
        LogUtils.d(msg);
        RxBus.getInstance().post(msg);
    }

    /**
     * 绑定所有类型的消息通知，具体类型根据PObserver#onMessage的key进行区分
     *
     * @param observer 事件回调
     */
    public void attach(final PObserver observer) {
        attach(observer, false);
    }

    public void attach(final PObserver observer, boolean isReLoad) {
        if (observer == null) {
            return;
        }
        if (!isReLoad) {//是否可以重复添加同一个类 ，在检测网络状态时，在添加时不需要移除之前的类
            //移除前一个添加的相同的类
            PObserver preObserver = classObserverMap.get(observer.getClass());
            if (preObserver != null) {
                detach(preObserver);
            }
        }
        Subscription subscription = RxBus.getInstance().toObservable(Msg.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Msg>() {
                    @Override
                    public void call(Msg msg) {
                        observer.onMessage(msg.getKey(), msg.getData());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable);
                    }
                });
        classObserverMap.put(observer.getClass(), observer);
        observerDisposableMap.put(observer, subscription);
        mCompositeSubscription.add(subscription);
    }

    /**
     * 解除绑定消息通知
     *
     * @param observer 监听
     */
    public void detach(PObserver observer) {
        if (observer == null) {
            return;
        }
        Subscription subscription = observerDisposableMap.remove(observer);
        if (subscription != null) {
            mCompositeSubscription.remove(subscription);
            LogUtils.d(mCompositeSubscription);
        }
        classObserverMap.remove(observer.getClass());
    }

    /**
     * 移除集合中所有的请求
     */
    public void removeAll() {
        if (observerDisposableMap.isEmpty()) {
            return;
        }
        for (PObserver pObserver : observerDisposableMap.keySet()) {
            Subscription subscription = observerDisposableMap.remove(pObserver);
            if (subscription != null) {
                mCompositeSubscription.remove(subscription);
            }
        }
        classObserverMap.clear();
    }
}
