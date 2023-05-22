public class Main {
    public static void main(String[] args) {
        EmptyMethod.doNothing();
        DuplicatedCode.print("Some message");
        int increasedNumber = InlineExpansion.increase(200);
        int randomInt = UnusedMethod.getRandomInt();
        int sum = RedundantVariables.sum(1, 2, 3);
        BubbleSort.sort(new Integer[] {increasedNumber, randomInt, sum, 6, 5, 4, 3, 2, 1});
    }
}
