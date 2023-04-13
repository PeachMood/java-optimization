@SuppressWarnings("SpellCheckingInspection")
public class JniRGBImage implements RGBImage, AutoCloseable {
  private final long rgbImagePtr;
  private boolean closed;

  public JniRGBImage(RGBPixel[][] image) {
    this.rgbImagePtr = init(image);
    this.closed = false;
  }

  @Override
  public RGBPixel[][] image() {
    return getImage(this.rgbImagePtr);
  }

  @Override
  public RGBPixel getPixel(int x, int y) {
    return getPixel(this.rgbImagePtr, x, y);
  }

  @Override
  public void setPixel(int x, int y, RGBPixel pixel) {
    setPixel(this.rgbImagePtr, x, y, pixel);
  }

  @Override
  public void blur() {
    blur(this.rgbImagePtr);
  }

  @Override
  public void close() {
    if (!this.closed) {
      destroy(this.rgbImagePtr);
      this.closed = true;
    }
  }

  private native long init(RGBPixel[][] image);

  private native RGBPixel[][] getImage(long imagePtr);

  private native RGBPixel getPixel(long imagePtr, int x, int y);

  private native void setPixel(long imagePtr, int x, int y, RGBPixel pixel);

  private native void blur(long imagePtr);

  private native void destroy(long imagePtr);

  static {
    System.loadLibrary("libjni");
  }
}
