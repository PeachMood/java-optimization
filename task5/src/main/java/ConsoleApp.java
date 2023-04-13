import java.util.Arrays;
import java.util.Random;

public class ConsoleApp {
  private static final Random random = new Random();
  private static final int N = 4096;
  private static final int M = 4096;

  public static void main(String[] args) {
    ExecutionTime executionTime = new ExecutionTime();
    RGBPixel[][] image = whiteNoiseImage(N, M);
    RGBImage javaRGBImage = new JavaRGBImage(image);
    try (JniRGBImage jniRGBImage = new JniRGBImage(image)) {
      System.out.println("Blur with java method: " + executionTime.measure(javaRGBImage::blur));
      System.out.println("Blur with jni method: " + executionTime.measure(jniRGBImage::blur));
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static RGBPixel[][] whiteNoiseImage(int n, int m) {
    RGBPixel[][] image = new RGBPixel[m][];
    for (int i = 0; i < m; i++) {
      image[i] = new RGBPixel[n];
      Arrays.setAll(image[i], idx -> new RGBPixel(nextGaussianByte(), nextGaussianByte(), nextGaussianByte()));
    }
    return image;
  }

  private static byte nextGaussianByte() {
    int result = (int) Math.round(64 * random.nextGaussian());
    while (result < -127 || result > 127) {
      result = (int) Math.round(64 * random.nextGaussian());
    }
    return (byte) result;
  }
}
