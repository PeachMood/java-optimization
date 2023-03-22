public class Main {
    public static void main(String[] args) {
        NativeLibrary library = new NativeLibrary();
//        library.allocateMemory();
//        library.divideByZero();

        String string = "Hello, World!";
        System.out.println("Length of the string \"" + string + "\": " + library.getLength(string));

        TestClass testClass = new TestClass();
        System.out.println("Returned value from class method: " + library.invokeClassMethod(testClass));

        System.out.println("Initial class field value: " + testClass.testField);
        library.setClassField(testClass, 100);
        System.out.println("Changed class field value: " + testClass.testField);

        long nativeStructure = library.allocateStructure();
        System.out.println("Native structure field value: " + library.getStructureField(nativeStructure));
        library.freeMemory(nativeStructure);
    }
}
