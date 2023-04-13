public class BubbleSort {
    public static <T extends Comparable<T>> T[] sort(T[] array) {
        T[] sorted = array.clone();
        for (int i = sorted.length; i > 1; i--) {
            for (int j = 0; j < i - 1; j++) {
                if (sorted[j].compareTo(sorted[j + 1]) > 0) {
                    T temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        return sorted;
    }
}
