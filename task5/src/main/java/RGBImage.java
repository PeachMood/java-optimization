public interface RGBImage {
  RGBPixel[][] image();

  RGBPixel getPixel(int x, int y);

  void setPixel(int x, int y, RGBPixel pixel);

  void blur();
}
