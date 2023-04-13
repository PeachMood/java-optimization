public record JavaRGBImage(RGBPixel[][] image) implements RGBImage {
  public JavaRGBImage(RGBPixel[][] image) {
    this.image = new RGBPixel[image.length][];
    for (int i = 0; i < image.length; i++) {
      this.image[i] = new RGBPixel[image[i].length];
      System.arraycopy(image[i], 0, this.image[i], 0, image[i].length);
    }
  }

  @Override
  public RGBPixel[][] image() {
    RGBPixel[][] image = new RGBPixel[this.image.length][];
    for (int i = 0; i < this.image.length; i++) {
      image[i] = new RGBPixel[this.image[i].length];
      System.arraycopy(this.image[i], 0, image[i], 0, this.image[i].length);
    }
    return image;
  }

  @Override
  public RGBPixel getPixel(int x, int y) {
    return this.image[y][x];
  }

  @Override
  public void setPixel(int x, int y, RGBPixel pixel) {
    this.image[y][x] = pixel;
  }

  @Override
  public void blur() {
    RGBPixel[][] image = new RGBPixel[this.image.length][];
    for (int i = 0; i < this.image.length; i++) {
      image[i] = new RGBPixel[this.image[i].length];
      for (int j = 0; j < this.image[i].length; j++) {
        int count = 0;
        int blue = 0;
        int green = 0;
        int red = 0;
        if (i > 0 && j > 0) {
          blue += this.image[i - 1][j - 1].blue();
          green += this.image[i - 1][j - 1].green();
          red += this.image[i - 1][j - 1].red();
          count += 1;
        }
        if (i > 0) {
          blue += this.image[i - 1][j].blue();
          green += this.image[i - 1][j].green();
          red += this.image[i - 1][j].red();
          count += 1;
        }
        if (i > 0 && j < this.image[i - 1].length - 1) {
          blue += this.image[i - 1][j + 1].blue();
          green += this.image[i - 1][j + 1].green();
          red += this.image[i - 1][j + 1].red();
          count += 1;
        }
        if (j > 0) {
          blue += this.image[i][j - 1].blue();
          green += this.image[i][j - 1].green();
          red += this.image[i][j - 1].red();
          count += 1;
        }
        if (j < this.image[i].length - 1) {
          blue += this.image[i][j + 1].blue();
          green += this.image[i][j + 1].green();
          red += this.image[i][j + 1].red();
          count += 1;
        }
        if (i < this.image.length - 1 && j > 0) {
          blue += this.image[i + 1][j - 1].blue();
          green += this.image[i + 1][j - 1].green();
          red += this.image[i + 1][j - 1].red();
          count += 1;
        }
        if (i < this.image.length - 1) {
          blue += this.image[i + 1][j].blue();
          green += this.image[i + 1][j].green();
          red += this.image[i + 1][j].red();
          count += 1;
        }
        if (i < this.image.length - 1 && j < this.image[i + 1].length - 1) {
          blue += this.image[i + 1][j + 1].blue();
          green += this.image[i + 1][j + 1].green();
          red += this.image[i + 1][j + 1].red();
          count += 1;
        }
        image[i][j] = new RGBPixel((byte) (blue / count), (byte) (red / count),
            (byte) (green / count));
      }
    }
    System.arraycopy(image, 0, this.image, 0, image.length);
  }
}
