package com.lester.jni;

/**
 * @Author lester
 * @Date 2018/8/1
 */
public class NdkTools {
    static {
        System.loadLibrary("jni-test");
    }
    public static native String getStringFromNDK();
}
