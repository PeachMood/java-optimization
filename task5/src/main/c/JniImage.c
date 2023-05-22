#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct Pixel
{
  int red;
  int green;
  int blue;
} Pixel;

typedef struct Image
{
  Pixel ***pixels;
  int width;
  int height;
} Image;

JNIEXPORT jlong JNICALL Java_JniImage_setPixelsJni(JNIEnv *env, jobject this, jobjectArray pixels)
{
  Image *image = (Image *)calloc(1, sizeof(Image));
  image->height = (int)((*env)->GetArrayLength(env, pixels));
  image->pixels = (Pixel ***)malloc(image->height * sizeof(Pixel **));
  jmethodID blueMethodID = NULL;
  jmethodID greenMethodID = NULL;
  jmethodID redMethodID = NULL;
  for (int i = 0; i < image->height; i++)
  {
    jobjectArray row = (jobjectArray)((*env)->GetObjectArrayElement(env, pixels, (jsize)i));
    if (image->width == 0)
    {
      image->width = (int)((*env)->GetArrayLength(env, row));
    }
    image->pixels[i] = (Pixel **)malloc(image->width * sizeof(Pixel *));
    for (int j = 0; j < image->width; j++)
    {
      jobject pixel = (*env)->GetObjectArrayElement(env, row, (jsize)j);
      if (blueMethodID == NULL && greenMethodID == NULL && redMethodID == NULL)
      {
        jclass pixelClass = (*env)->GetObjectClass(env, pixel);
        blueMethodID = (*env)->GetMethodID(env, pixelClass, "blue", "()I");
        greenMethodID = (*env)->GetMethodID(env, pixelClass, "green", "()I");
        redMethodID = (*env)->GetMethodID(env, pixelClass, "red", "()I");
        (*env)->DeleteLocalRef(env, pixelClass);
      }
      image->pixels[i][j] = (Pixel *)malloc(sizeof(Pixel));
      image->pixels[i][j]->blue = (int)((*env)->CallByteMethod(env, pixel, blueMethodID));
      image->pixels[i][j]->green = (int)((*env)->CallByteMethod(env, pixel, greenMethodID));
      image->pixels[i][j]->red = (int)((*env)->CallByteMethod(env, pixel, redMethodID));
      (*env)->DeleteLocalRef(env, pixel);
    }
    (*env)->DeleteLocalRef(env, row);
  }
  (*env)->DeleteLocalRef(env, blueMethodID);
  (*env)->DeleteLocalRef(env, greenMethodID);
  (*env)->DeleteLocalRef(env, redMethodID);
  return (jlong)image;
}

JNIEXPORT jobjectArray JNICALL Java_JniImage_getPixelsJni(JNIEnv *env, jobject this, jlong rgbImagePtr)
{
  Image *rgbImage = (Image *)rgbImagePtr;
  jclass imageClass = (*env)->FindClass(env, "[LPixel;");
  jobjectArray image = (*env)->NewObjectArray(env, (jsize)rgbImage->height, imageClass, NULL);
  (*env)->DeleteLocalRef(env, imageClass);
  jclass imageRowClass = (*env)->FindClass(env, "LPixel;");
  jclass pixelClass = (*env)->FindClass(env, "LPixel;");
  jmethodID initMethodID = (*env)->GetMethodID(env, pixelClass, "<init>", "(III)V");
  for (int i = 0; i < rgbImage->height; i++)
  {
    jobjectArray imageRow = (*env)->NewObjectArray(env, rgbImage->width, imageRowClass, NULL);
    for (int j = 0; j < rgbImage->width; j++)
    {

      jobject pixel = (*env)->NewObject(env, pixelClass, initMethodID, (jint)rgbImage->pixels[i][j]->red, (jint)rgbImage->pixels[i][j]->green, (jint)rgbImage->pixels[i][j]->blue);
      (*env)->SetObjectArrayElement(env, imageRow, (jsize)j, pixel);
      (*env)->DeleteLocalRef(env, pixel);
    }
    (*env)->SetObjectArrayElement(env, image, (jsize)i, imageRow);
    (*env)->DeleteLocalRef(env, imageRow);
  }
  (*env)->DeleteLocalRef(env, imageRowClass);
  (*env)->DeleteLocalRef(env, pixelClass);
  (*env)->DeleteLocalRef(env, initMethodID);
  return image;
}

Pixel ***GetImage(Image *rgbImage)
{
  Pixel ***image = (Pixel ***)malloc(rgbImage->height * sizeof(Pixel **));
  for (int i = 0; i < rgbImage->height; i++)
  {
    image[i] = (Pixel **)malloc(rgbImage->width * sizeof(Pixel *));
    memcpy(image[i], rgbImage->pixels[i], rgbImage->width * sizeof(Pixel *));
  }
  return image;
}

Pixel *GetPixel(Image *rgbImage, int x, int y)
{
  return rgbImage->pixels[y][x];
}

JNIEXPORT jobject JNICALL Java_JniImage_getPixelJni(JNIEnv *env, jobject this, jlong image, jint x, jint y)
{
  Pixel *rgbPixel = GetPixel((Image *)image, (int)x, (int)y);
  jclass pixelClass = (*env)->FindClass(env, "Lio/Pixel;");
  jmethodID initMethodID = (*env)->GetMethodID(env, pixelClass, "<init>", "(III)V");
  jobject pixel = (*env)->NewObject(env, pixelClass, initMethodID, (jint)rgbPixel->red, (jint)rgbPixel->green, (jint)rgbPixel->blue);
  (*env)->DeleteLocalRef(env, pixelClass);
  (*env)->DeleteLocalRef(env, initMethodID);
  return pixel;
}

void SetPixel(Image *rgbImage, int x, int y, Pixel *pixel)
{
  rgbImage->pixels[y][x] = pixel;
}

JNIEXPORT void JNICALL Java_JniImage_setPixelJni(JNIEnv *env, jobject this, jlong image, jint x, jint y, jobject pixel)
{
  jclass pixelClass = (*env)->GetObjectClass(env, pixel);
  jmethodID blueMethodID = (*env)->GetMethodID(env, pixelClass, "blue", "()I");
  jmethodID greenMethodID = (*env)->GetMethodID(env, pixelClass, "green", "()I");
  jmethodID redMethodID = (*env)->GetMethodID(env, pixelClass, "red", "()I");
  (*env)->DeleteLocalRef(env, pixelClass);
  Pixel *rgbPixel = (Pixel *)malloc(sizeof(Pixel));
  rgbPixel->blue = (int)((*env)->CallByteMethod(env, pixel, blueMethodID));
  rgbPixel->green = (int)((*env)->CallByteMethod(env, pixel, greenMethodID));
  rgbPixel->red = (int)((*env)->CallByteMethod(env, pixel, redMethodID));
  (*env)->DeleteLocalRef(env, blueMethodID);
  (*env)->DeleteLocalRef(env, greenMethodID);
  (*env)->DeleteLocalRef(env, redMethodID);
  SetPixel((Image *)image, (int)x, (int)y, rgbPixel);
}

double GaussianModel(double x, double y, double variance)
{
  return (1 / (2 * M_PI * pow(variance, 2)) * exp(-(pow(x, 2) + pow(y, 2)) / (2 * pow(variance, 2))));
}

double **GenerateWeightMatrix(int radius, double variance)
{
  double **weights = (double **)malloc(radius * sizeof(double *));
  double summation = 0;

  for (int i = 0; i < radius; ++i)
  {
    weights[i] = (double *)malloc(radius * sizeof(double));
    for (int j = 0; j < radius; ++j)
    {
      weights[i][j] = GaussianModel(i - (double)radius / 2, j - (double)radius / 2, variance);
      summation += weights[i][j];
    }
  }

  for (int i = 0; i < radius; ++i)
  {
    for (int j = 0; j < radius; ++j)
    {
      weights[i][j] /= summation;
    }
  }

  return weights;
}

int GetWeightedPixelValue(int radius, double weightedColor[][radius])
{
  double summation = 0;

  for (int i = 0; i < radius; ++i)
  {
    for (int j = 0; j < radius; ++j)
    {
      summation += weightedColor[i][j];
    }
  }

  return (int)summation;
}

JNIEXPORT void JNICALL Java_JniImage_blurJni(JNIEnv *env, jobject this, jlong rgbImagePtr, jint radius)
{
  Image* rgbImage = (Image*) rgbImagePtr;
  Pixel ***image = (Pixel ***)malloc(rgbImage->height * sizeof(Pixel **));
  for (int i = 0; i < rgbImage->height; i++)
  {
    image[i] = (Pixel **)malloc(rgbImage->width * sizeof(Pixel *));
    for (int j = 0; j < rgbImage->width; j++)
    {
      int blue = 0;
      int green = 0;
      int red = 0;
      int count = 0;
      if (i > 0 && j > 0)
      {
        blue += rgbImage->pixels[i - 1][j - 1]->blue;
        green += rgbImage->pixels[i - 1][j - 1]->green;
        red += rgbImage->pixels[i - 1][j - 1]->red;
        count += 1;
      }
      if (i > 0)
      {
        blue += rgbImage->pixels[i - 1][j]->blue;
        green += rgbImage->pixels[i - 1][j]->green;
        red += rgbImage->pixels[i - 1][j]->red;
        count += 1;
      }
      if (i > 0 && j < rgbImage->width - 1)
      {
        blue += rgbImage->pixels[i - 1][j + 1]->blue;
        green += rgbImage->pixels[i - 1][j + 1]->green;
        red += rgbImage->pixels[i - 1][j + 1]->red;
        count += 1;
      }
      if (j > 0)
      {
        blue += rgbImage->pixels[i][j - 1]->blue;
        green += rgbImage->pixels[i][j - 1]->green;
        red += rgbImage->pixels[i][j - 1]->red;
        count += 1;
      }
      if (j < rgbImage->width - 1)
      {
        blue += rgbImage->pixels[i][j + 1]->blue;
        green += rgbImage->pixels[i][j + 1]->green;
        red += rgbImage->pixels[i][j + 1]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1 && j > 0)
      {
        blue += rgbImage->pixels[i + 1][j - 1]->blue;
        green += rgbImage->pixels[i + 1][j - 1]->green;
        red += rgbImage->pixels[i + 1][j - 1]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1)
      {
        blue += rgbImage->pixels[i + 1][j]->blue;
        green += rgbImage->pixels[i + 1][j]->green;
        red += rgbImage->pixels[i + 1][j]->red;
        count += 1;
      }
      if (i < rgbImage->height - 1 && j < rgbImage->width - 1)
      {
        blue += rgbImage->pixels[i + 1][j + 1]->blue;
        green += rgbImage->pixels[i + 1][j + 1]->green;
        red += rgbImage->pixels[i + 1][j + 1]->red;
        count += 1;
      }
      image[i][j] = (Pixel *)malloc(sizeof(Pixel));
      image[i][j]->blue = (int)(blue / count);
      image[i][j]->green = (int)(green / count);
      image[i][j]->red = (int)(red / count);
    }
  }
  for (int i = 0; i < rgbImage->height; i++)
  {
    for (int j = 0; j < rgbImage->width; j++)
    {
      free(rgbImage->pixels[i][j]);
    }
    free(rgbImage->pixels[i]);
  }
  memcpy(rgbImage->pixels, image, rgbImage->height * sizeof(Pixel **));
  free(image);
}

void DestroyImage(Image *rgbImage)
{
  for (int i = 0; i < rgbImage->height; i++)
  {
    for (int j = 0; j < rgbImage->width; j++)
    {
      free(rgbImage->pixels[i][j]);
    }
    free(rgbImage->pixels[i]);
  }
  free(rgbImage);
}

JNIEXPORT void JNICALL Java_JniImage_destroy(JNIEnv *env, jobject this, jlong image)
{
  DestroyImage((Image *)image);
}
