#include <jni.h>
#include <stdlib.h>

typedef struct RGBPixel {
  char blue;
  char green;
  char red;
} RGBPixel;

typedef struct RGBImage {
  RGBPixel*** image;
  int width;
  int height;
} RGBImage;

RGBImage* RGBImageInit(RGBPixel*** image, int width, int height) {
  RGBImage* rgbImage = (RGBImage*) malloc(sizeof(RGBImage));
  rgbImage->width = width;
  rgbImage->height = height;
  rgbImage->image = (RGBPixel***) malloc(height * sizeof(RGBPixel**));
  for (int i = 0; i < height; i++) {
    rgbImage->image[i] = (RGBPixel**) malloc(width * sizeof(RGBPixel*));
    memcpy(rgbImage->image[i], image[i], width * sizeof(RGBPixel*));
  }
  return rgbImage;
}

RGBPixel*** RGBImageGetImage(RGBImage* rgbImage) {
  RGBPixel*** image = (RGBPixel***) malloc(rgbImage->height * sizeof(RGBPixel**));
  for (int i = 0; i < rgbImage->height; i++) {
    image[i] = (RGBPixel**) malloc(rgbImage->width * sizeof(RGBPixel*));
    memcpy(image[i], rgbImage->image[i], rgbImage->width * sizeof(RGBPixel*));
  }
  return image;
}

RGBPixel* RGBImageGetPixel(RGBImage* rgbImage, int x, int y) {
  return rgbImage->image[y][x];
}

void RGBImageSetPixel(RGBImage* rgbImage, int x, int y, RGBPixel* pixel) {
  rgbImage->image[y][x] = pixel;
}

void RGBImageBlur(RGBImage* rgbImage) {
  RGBPixel*** image = (RGBPixel***) malloc(rgbImage->height * sizeof(RGBPixel**));
  for (int i = 0; i < rgbImage->height; i++) {
    image[i] = (RGBPixel**) malloc(rgbImage->width * sizeof(RGBPixel*));
    for (int j = 0; j < rgbImage->width; j++) {
      int blue = 0;
      int green = 0;
      int red = 0;
      int count = 0;
      if (i > 0 && j > 0) {
        blue += rgbImage->image[i - 1][j - 1]->blue;
        green += rgbImage->image[i - 1][j - 1]->green;
        red += rgbImage->image[i - 1][j - 1]->red;
        count += 1;
      }
      if (i > 0) {
        blue += rgbImage->image[i - 1][j]->blue;
        green += rgbImage->image[i - 1][j]->green;
        red += rgbImage->image[i - 1][j]->red;
        count += 1;
      }
      if (i > 0 && j < rgbImage->width - 1) {
        blue += rgbImage->image[i - 1][j + 1]->blue;
        green += rgbImage->image[i - 1][j + 1]->green;
        red += rgbImage->image[i - 1][j + 1]->red;
        count += 1;
      }
      if (j > 0) {
        blue += rgbImage->image[i][j - 1]->blue;
        green += rgbImage->image[i][j - 1]->green;
        red += rgbImage->image[i][j - 1]->red;
        count += 1;
      }
      if (j < rgbImage->width - 1) {
        blue += rgbImage->image[i][j + 1]->blue;
        green += rgbImage->image[i][j + 1]->green;
        red += rgbImage->image[i][j + 1]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1 && j > 0) {
        blue += rgbImage->image[i + 1][j - 1]->blue;
        green += rgbImage->image[i + 1][j - 1]->green;
        red += rgbImage->image[i + 1][j - 1]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1) {
        blue += rgbImage->image[i + 1][j]->blue;
        green += rgbImage->image[i + 1][j]->green;
        red += rgbImage->image[i + 1][j]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1 && j < rgbImage->width - 1) {
        blue += rgbImage->image[i + 1][j + 1]->blue;
        green += rgbImage->image[i + 1][j + 1]->green;
        red += rgbImage->image[i + 1][j + 1]->red;
        count += 1;
      }
      image[i][j] = (RGBPixel*) malloc(sizeof(RGBPixel));
      image[i][j]->blue = (char) (blue / count);
      image[i][j]->green = (char) (green / count);
      image[i][j]->red = (char) (red / count);
    }
  }
  for (int i = 0; i < rgbImage->height; i++) {
    for (int j = 0; j < rgbImage->width; j++) {
      free(rgbImage->image[i][j]);
    }
    free(rgbImage->image[i]);
  }
  memcpy(rgbImage->image, image, rgbImage->height * sizeof(RGBPixel**));
  free(image);
}

void RGBImageDestroy(RGBImage* rgbImage) {
  for (int i = 0; i < rgbImage->height; i++) {
    for (int j = 0; j < rgbImage->width; j++) {
      free(rgbImage->image[i][j]);
    }
    free(rgbImage->image[i]);
  }
  free(rgbImage);
}

JNIEXPORT jlong JNICALL Java_io_github_dtolmachev1_JniRGBImage_init(JNIEnv *env, jobject this, jobjectArray image) {
  RGBImage* rgbImage = (RGBImage*) calloc(1, sizeof(RGBImage));
  rgbImage->height = (int) ((*env)->GetArrayLength(env, image));
  rgbImage->image = (RGBPixel***) malloc(rgbImage->height * sizeof(RGBPixel**));
  jmethodID blueMethodID = NULL;
  jmethodID greenMethodID = NULL;
  jmethodID redMethodID = NULL;
    for (int i = 0; i < rgbImage->height; i++) {
    jobjectArray imageRow = (jobjectArray) ((*env)->GetObjectArrayElement(env, image, (jsize) i));
    if (rgbImage->width == 0) {
      rgbImage->width = (int) ((*env)->GetArrayLength(env, imageRow));
    }
    rgbImage->image[i] = (RGBPixel**) malloc(rgbImage->width * sizeof(RGBPixel*));
    for (int j = 0; j < rgbImage->width; j++) {
      jobject pixel = (*env)->GetObjectArrayElement(env, imageRow, (jsize) j);
      if (blueMethodID == NULL && greenMethodID == NULL && redMethodID == NULL) {
        jclass pixelClass = (*env)->GetObjectClass(env, pixel);
        blueMethodID = (*env)->GetMethodID(env, pixelClass, "blue", "()B");
        greenMethodID = (*env)->GetMethodID(env, pixelClass, "green", "()B");
        redMethodID = (*env)->GetMethodID(env, pixelClass, "red", "()B");
        (*env)->DeleteLocalRef(env, pixelClass);
      }
      rgbImage->image[i][j] = (RGBPixel*) malloc(sizeof(RGBPixel));
      rgbImage->image[i][j]->blue = (char) ((*env)->CallByteMethod(env, pixel, blueMethodID));
      rgbImage->image[i][j]->green = (char) ((*env)->CallByteMethod(env, pixel, greenMethodID));
      rgbImage->image[i][j]->red = (char) ((*env)->CallByteMethod(env, pixel, redMethodID));
      (*env)->DeleteLocalRef(env, pixel);
    }
    (*env)->DeleteLocalRef(env, imageRow);
  }
  (*env)->DeleteLocalRef(env, blueMethodID);
  (*env)->DeleteLocalRef(env, greenMethodID);
  (*env)->DeleteLocalRef(env, redMethodID);
  return (jlong) rgbImage;
}

JNIEXPORT jobjectArray JNICALL Java_io_github_dtolmachev1_JniRGBImage_getImage(JNIEnv *env, jobject this, jlong rgbImagePtr) {
  RGBImage* rgbImage = (RGBImage*) rgbImagePtr;
  jclass imageClass = (*env)->FindClass(env, "[[Lio/github/dtolmachev1/RGBPixel;");
  jobjectArray image = (*env)->NewObjectArray(env, (jsize) rgbImage->height, imageClass, NULL);
  (*env)->DeleteLocalRef(env, imageClass);
  jclass imageRowClass = (*env)->FindClass(env, "[Lio/github/dtolmachev1/RGBPixel;");
  jclass pixelClass = (*env)->FindClass(env, "Lio/github/dtolmachev1/RGBPixel;");
  jmethodID initMethodID = (*env)->GetMethodID(env, pixelClass, "init", "(BBB)V");
  for (int i = 0; i < rgbImage->height; i++) {
    jobjectArray imageRow = (*env)->NewObjectArray(env, rgbImage->width, imageRowClass, NULL);
    for (int j = 0; j < rgbImage->width; j++) {
      jobject pixel = (*env)->NewObject(env, pixelClass, initMethodID, (jbyte) rgbImage->image[i][j]->blue, (jbyte) rgbImage->image[i][j]->green, (jbyte) rgbImage->image[i][j]->red);
      (*env)->SetObjectArrayElement(env, imageRow, (jsize) j, pixel);
      (*env)->DeleteLocalRef(env, pixel);
    }
    (*env)->SetObjectArrayElement(env, image, (jsize) i, imageRow);
    (*env)->DeleteLocalRef(env, imageRow);
  }
  (*env)->DeleteLocalRef(env, imageRowClass);
  (*env)->DeleteLocalRef(env, pixelClass);
  (*env)->DeleteLocalRef(env, initMethodID);
  return image;
}

JNIEXPORT jobject JNICALL Java_io_github_dtolmachev1_JniRGBImage_getPixel(JNIEnv *env, jobject this, jlong rgbImagePtr, jint x, jint y) {
  RGBPixel* rgbPixel = RGBImageGetPixel((RGBImage*) rgbImagePtr, (int) x, (int) y);
  jclass pixelClass = (*env)->FindClass(env, "Lio/github/dtolmachev1/RGBPixel;");
  jmethodID initMethodID = (*env)->GetMethodID(env, pixelClass, "init", "(BBB)V");
  jobject pixel = (*env)->NewObject(env, pixelClass, initMethodID, (jbyte) rgbPixel->blue, (jbyte) rgbPixel->green, (jbyte) rgbPixel->red);
  (*env)->DeleteLocalRef(env, pixelClass);
  (*env)->DeleteLocalRef(env, initMethodID);
  return pixel;
}

JNIEXPORT void JNICALL Java_io_github_dtolmachev1_JniRGBImage_setPixel(JNIEnv *env, jobject this, jlong rgbImagePtr, jint x, jint y, jobject pixel) {
  jclass pixelClass = (*env)->GetObjectClass(env, pixel);
  jmethodID blueMethodID = (*env)->GetMethodID(env, pixelClass, "blue", "()B");
  jmethodID greenMethodID = (*env)->GetMethodID(env, pixelClass, "green", "()B");
  jmethodID redMethodID = (*env)->GetMethodID(env, pixelClass, "red", "()B");
  (*env)->DeleteLocalRef(env, pixelClass);
  RGBPixel* rgbPixel = (RGBPixel*) malloc(sizeof(RGBPixel));
  rgbPixel->blue = (char) ((*env)->CallByteMethod(env, pixel, blueMethodID));
  rgbPixel->green = (char) ((*env)->CallByteMethod(env, pixel, greenMethodID));
  rgbPixel->red = (char) ((*env)->CallByteMethod(env, pixel, redMethodID));
  (*env)->DeleteLocalRef(env, blueMethodID);
  (*env)->DeleteLocalRef(env, greenMethodID);
  (*env)->DeleteLocalRef(env, redMethodID);
  RGBImageSetPixel((RGBImage*) rgbImagePtr, (int) x, (int) y, rgbPixel);
}

JNIEXPORT void JNICALL Java_io_github_dtolmachev1_JniRGBImage_blur(JNIEnv *env, jobject this, jlong rgbImagePtr) {
  RGBImageBlur((RGBImage*) rgbImagePtr);
}

JNIEXPORT void JNICALL Java_io_github_dtolmachev1_JniRGBImage_destroy(JNIEnv *env, jobject this, jlong rgbImagePtr) {
  RGBImageDestroy((RGBImage*) rgbImagePtr);
}
