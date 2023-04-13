package com.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    int threadsNumber = 100;
    List<Thread> threads = new ArrayList<>(threadsNumber);
    for (int i = 0; i < threadsNumber; ++i) {
      Thread thread = new InfiniteSleepThread();
      threads.add(thread);
      thread.start();
    }

    threads.forEach(thread -> thread.interrupt());
  }
}
