public class DuplicatedCode {
    public static void print(String string) {
        System.out.println(spoil(change(string)));
    }

    private static String spoil(String string) {
        StringBuilder stringBuilder;
        if (string == null) {
            stringBuilder =  new StringBuilder();
        } else {
            stringBuilder = new StringBuilder(string);
        }
        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(" ");
            stringBuilder.append("[ This class has duplicates! ]");
        }
        return stringBuilder.toString();
    }

    private static String change(String string) {
        StringBuilder stringBuilder;
        if (string == null) {
            stringBuilder =  new StringBuilder();
        } else {
            stringBuilder = new StringBuilder(string);
        }
        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(" ");
            stringBuilder.append("[ This class has duplicates! ]");
        }
        return stringBuilder.toString();
    }
}
