import java.util.Arrays;
import java.util.Random;

public class Example {
    public static int getLength(String string) {
        return string.length();
    }

    public static void invokeMethod(Pair<String, Integer> pair)  {
        pair.setValue(1000);
    }

//    @SuppressWarnings("unchecked")
//    public static void sortArray() {
//        Random random = new Random();
//        Pair<String, Integer>[] array = new Pair[10];
//        for (int i = 0; i < 100; ++i) {
//            array[i] = (new Pair<>("Name " + i, random.nextInt(200)));
//        }
//
//        Pair<String, Integer>[] sorted = BubbleSort.sort(array);
//        Arrays.stream(sorted).forEach(pair -> System.out.print(pair.toString() + " "));
//     }


}
