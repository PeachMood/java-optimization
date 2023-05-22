public class InlineExpansion {
    public static int increase(int number) {
        return multiplyByTwo(number);
    }

    private static int multiplyByTwo(int number) {
        return 2 * number;
    }
}
