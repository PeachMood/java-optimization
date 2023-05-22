public class RedundantVariables {
    public static int sum(int a, int b, int c) {
        int d = a + b;
        int e = d + c;
        return e;
    }
}
