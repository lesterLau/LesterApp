#include "com_lester_jni_NdkTools.h"

JNIEXPORT jstring JNICALL Java_com_lester_jni_NdkTools_getStringFromNDK
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "Hello World");
}