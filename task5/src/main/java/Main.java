import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        testTime();
    }

    public static void testBlur() throws IOException {
        JavaImage image = new JavaImage();
        image.load(new File("img.png"));
        image.blur();
        image.save(new File("javablurred.jpg"));

        JniImage jniImage = new JniImage();
        jniImage.load(new File("img.png"));
        jniImage.blur();
        jniImage.save(new File("jniblurred.jpg"));
    }

    public static void testTime() {
        ExecutionTime executionTime = new ExecutionTime();
        Pixel[][] image = whiteNoisePixels(4096, 4096);
        Image javaImage = new JavaImage(image);
        try (JniImage jniImage = new JniImage(image)) {
            System.out.println("Blur with java method: " + executionTime.measure(javaImage::blur));
            System.out.println("Blur with jni method: " + executionTime.measure(jniImage::blur));
        }
    }

    private static Pixel[][] whiteNoisePixels(int n, int m) {
        Pixel[][] image = new Pixel[m][];
        for (int i = 0; i < m; i++) {
            image[i] = new Pixel[n];
            Arrays.setAll(image[i], idx -> new Pixel(nextGaussianByte(), nextGaussianByte(), nextGaussianByte()));
        }
        return image;
    }

    private static byte nextGaussianByte() {
        Random random = new Random();
        int result = (int) Math.round(64 * random.nextGaussian());
        while (result < -127 || result > 127) {
            result = (int) Math.round(64 * random.nextGaussian());
        }
        return (byte) result;
    }
}
