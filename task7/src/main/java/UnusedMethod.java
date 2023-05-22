import java.util.Random;

public class UnusedMethod {
    public static int getRandomInt() {
        return new Random().nextInt();
    }

    private static double unusedMethod() {
        return new Random().nextDouble();
    }
}
