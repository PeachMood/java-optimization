public class JniImage implements Image, AutoCloseable {
    private long image = 0;
    private boolean closed;

    public JniImage() {
    }

    public JniImage(Pixel[][] image) {
        setPixels(image);
        this.closed = false;
    }

    @Override
    public Pixel getPixel(int x, int y) {
        return getPixelJni(image, x, y);
    }

    @Override
    public Pixel[][] getPixels() {
        return getPixelsJni(image);
    }

    @Override
    public void setPixel(int x, int y, Pixel pixel) {
        setPixelJni(image, x, y, pixel);
    }

    @Override
    public void setPixels(Pixel[][] pixels) {
        image = setPixelsJni(pixels);
    }

    @Override
    public void blur() {
        blurJni(image, 5);
    }

    @Override
    public void close() {
        if (!this.closed) {
            destroy(this.image);
            this.closed = true;
        }
    }

    private native long setPixelsJni(Pixel[][] pixels);

    private native Pixel[][] getPixelsJni(long image);

    private native Pixel getPixelJni(long image, int x, int y);

    private native void setPixelJni(long image, int x, int y, Pixel pixel);

    private native void blurJni(long image, int radius);

    private native void destroy(long image);

    static {
        System.load("/home/ann/projects/java-optimization/task5/src/main/c/jniimage.so");
    }
}
