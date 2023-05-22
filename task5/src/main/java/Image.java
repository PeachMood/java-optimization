import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public interface Image {
    Pixel getPixel(int x, int y);

    Pixel[][] getPixels();

    void setPixels(Pixel[][] pixels);

    void setPixel(int x, int y, Pixel pixel);

    void blur();

    default void load(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        Pixel[][] pixels = readPixels(image);
        setPixels(pixels);
    }

    default void save(File file) throws IOException {
        Pixel[][] pixels = getPixels();
        BufferedImage image = createImage(pixels);
        ImageIO.write(image, "jpg", file);
    }

    private int getRgb(int red, int green, int blue) {
        return ((0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF));
    }

    private Pixel getPixel(int red, int green, int blue) {
        int rgb = getRgb(red, green, blue);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;
        return new Pixel(r, g, b);
    }

    private Pixel[][] readPixels(BufferedImage image) {
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        int width = image.getWidth();
        int height = image.getHeight();
        Pixel[][] pixels = new Pixel[height][width];

        int row = 0;
        int column = 0;
        for (int pixel = 0; pixel + 2 < data.length; pixel += 3) {
            pixels[row][column] = getPixel(data[pixel + 2], data[pixel + 1], data[pixel]);
            column++;
            if (column == width) {
                column = 0;
                row++;
            }
        }

        return pixels;
    }

    private BufferedImage createImage(Pixel[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int column = 0; column < width; ++column) {
            for (int row = 0; row < height; ++row) {
                Pixel pixel = pixels[row][column];
                int rgb = getRgb(pixel.red(), pixel.green(), pixel.blue());
                image.setRGB(column, row, rgb);
            }
        }
        return image;
    }
}
