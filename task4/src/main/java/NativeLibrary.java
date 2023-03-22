public class NativeLibrary {
    static {
        System.load("/home/ann/projects/java-optimization/task4/src/main/c/libnative.so");
    }

    public native void allocateMemory();
    public native void divideByZero();
    public native int getLength(String str);
    public native int invokeClassMethod(TestClass testClass);
    public native void setClassField(TestClass testClass, int value);
    public native long allocateStructure();
    public native int getStructureField(long structure);
    public native void freeMemory(long structure);
}
