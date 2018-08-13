package com.lesterlau.http;


import java.util.concurrent.ConcurrentHashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by liubin on 2016/9/19.
 */
public class RxManager {

    private static RxManager instance;

    private CompositeSubscription mCompositeSubscription;
    private ConcurrentHashMap<Object, Subscription> maps;

    private RxManager() {
        mCompositeSubscription = new CompositeSubscription();
        maps = new ConcurrentHashMap<>();
    }

    public static RxManager getInstance() {
        if (instance == null) {
            synchronized (RxManager.class) {
                if (instance == null) {
                    instance = new RxManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加
     */
    public void add(Object tag, Subscription subscription) {
        mCompositeSubscription.add(subscription);
        maps.put(tag, subscription);
    }

    /**
     * 根据tag标签将此请求移除出集合
     */
    public void remove(Object tag) {
        if (maps.get(tag) != null) {
            mCompositeSubscription.remove(maps.get(tag));
        }
    }

    /**
     * 移除集合中所有的请求
     */
    public void removeAll() {
        if (maps.isEmpty()) {
            return;
        }
        for (Object tag : maps.keySet()) {
            remove(tag);
        }
    }
}