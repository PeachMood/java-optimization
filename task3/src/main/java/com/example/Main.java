package com.example;

public class Main {
    public static void main(String[] args) {
        Runnable target = () -> {
            Singleton singleton = Singleton.getInstance();
            Bean bean = new Bean();
            try {
                Thread.sleep(1000000);
            } catch (Exception ignored) {
            }
        };

        for (int i = 0; i < 10; ++i) {
            Thread thread = new Thread(target);
            thread.start();
        }
    }
}
