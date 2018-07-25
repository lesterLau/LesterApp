package com.lester.lesterapp.observe;

/**
 * RxBus事件监听回调
 *
 * @Author lester
 * @Date 2018/7/24
 */
public interface PObserver {

    /**
     * 事件回调
     *
     * @param key   事件key
     * @param value 事件传递值
     */
    void onMessage(String key, Object value);
}
