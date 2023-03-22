#include "NativeLibrary.h"
#include <stdlib.h>
#include <string.h>
#include <limits.h>

JNIEXPORT void JNICALL Java_NativeLibrary_allocateMemory(JNIEnv *env, jobject obj) {
    while(1) {
        int* array = (int*) malloc(LONG_MAX);
        array[100] = 5;
    }
};

JNIEXPORT void JNICALL Java_NativeLibrary_divideByZero(JNIEnv *env, jobject obj) {
    int zero = 0;
    int i = 1000 / zero;
}

JNIEXPORT jint JNICALL Java_NativeLibrary_getLength(JNIEnv *env, jobject this, jstring str) {
    const char *string = (*env)->GetStringUTFChars(env, str, NULL);
    return strlen(string);
}

JNIEXPORT jint JNICALL Java_NativeLibrary_invokeClassMethod(JNIEnv *env, jobject this, jobject obj) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "testMethod", "()I");
    if (mid == 0) {
        return -1;
    }
    jint num = (*env)->CallIntMethod(env, obj, mid);
    return num;
}

JNIEXPORT void JNICALL Java_NativeLibrary_setClassField(JNIEnv *env, jobject this, jobject obj, jint i) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "testField", "I");
    if (fid == 0) {
        return;
    }
    (*env)->SetIntField(env, obj, fid, i);
}

typedef struct _Structure {
  int field;
} Structure;

JNIEXPORT jlong JNICALL Java_NativeLibrary_allocateStructure(JNIEnv *env, jobject obj) {
    Structure* structure = (Structure*) malloc(sizeof(Structure));
    structure->field = 500;
    return (jlong) structure;
}

JNIEXPORT jint JNICALL Java_NativeLibrary_getStructureField(JNIEnv *env, jobject obj, jlong lng) {
    Structure* structure = (Structure*) lng;
    return structure->field;
}

JNIEXPORT void JNICALL Java_NativeLibrary_freeMemory(JNIEnv *env, jobject obj, jlong lng) {
    Structure* structure = (Structure*) lng;
    free(structure);
}

