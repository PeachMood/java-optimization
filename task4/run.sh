javac -h ./src/main/c ./src/main/java/NativeLibrary.java ./src/main/java/TestClass.java
gcc -shared ./src/main/c/NativeLibrary.c -o ./src/main/c/libnative.so -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux/
java -Djava.library.path=./libs/ -cp ./build/classes/java/main Main
