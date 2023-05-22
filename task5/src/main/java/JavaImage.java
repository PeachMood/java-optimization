public class JavaImage implements Image {
    private Pixel[][] image;

    public JavaImage() {
    }

    public JavaImage(Pixel[][] image) {
        setPixels(image);
    }

    @Override
    public Pixel[][] getPixels() {
        Pixel[][] image = new Pixel[this.image.length][];
        for (int i = 0; i < this.image.length; i++) {
            image[i] = new Pixel[this.image[i].length];
            System.arraycopy(this.image[i], 0, image[i], 0, this.image[i].length);
        }
        return image;
    }

    @Override
    public void setPixels(Pixel[][] image) {
        this.image = new Pixel[image.length][];
        for (int i = 0; i < image.length; i++) {
            this.image[i] = new Pixel[image[i].length];
            System.arraycopy(image[i], 0, this.image[i], 0, image[i].length);
        }
    }

    @Override
    public Pixel getPixel(int x, int y) {
        return this.image[y][x];
    }

    @Override
    public void setPixel(int x, int y, Pixel pixel) {
        this.image[y][x] = pixel;
    }

    @Override
    public void blur() {
        Pixel[][] image = new Pixel[this.image.length][];
        for (int i = 0; i < this.image.length; i++) {
            image[i] = new Pixel[this.image[i].length];
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
                image[i][j] = new Pixel((red / count), (green / count), (blue / count));
            }
        }
        setPixels(image);
    }

    private int getWeightedPixelValue(double[][] weightedColor, int radius) {
        double summation = 0;

        for (int i = 0; i < radius; ++i) {
            for (int j = 0; j < radius; ++j) {
                summation += weightedColor[i][j];
            }
        }

        return (int) summation;
    }

    private double[][] generateWeightMatrix(int radius, double variance) {
        double[][] weights = new double[radius][radius];
        double summation = 0;

        for (int i = 0; i < radius; ++i) {
            for (int j = 0; j < radius; ++j) {
                weights[i][j] = gaussianModel(i - (double) radius / 2, j - (double) radius / 2, variance);
                summation += weights[i][j];
            }
        }

        for (int i = 0; i < radius; ++i) {
            for (int j = 0; j < radius; ++j) {
                weights[i][j] /= summation;
            }
        }

        return weights;
    }

    private double gaussianModel(double x, double y, double variance) {
        return (1 / (2 * Math.PI * Math.pow(variance, 2)) * Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
    }
}
