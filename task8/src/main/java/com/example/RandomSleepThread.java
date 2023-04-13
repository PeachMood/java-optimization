package com.example;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomSleepThread extends Thread {
  private final Random random = new Random();

  private void randomSleep() {
    long product = 1;
    for (int i = 0; i < 100000; ++i) {
      product *= random.nextInt(1);
      Thread.yield();
    }
    secondSleep();
  }

  private void secondSleep() {
    long product = 1;
    for (int i = 0; i < 200000; ++i) {
      product *= random.nextInt(1);
      Thread.yield();
    }
    thirdSleep();
  }

  private void thirdSleep() {
    long product = 1;
    for (int i = 0; i < 300000; ++i) {
      product *= random.nextInt(1);
      Thread.yield();
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(1);
        randomSleep();
      } catch (InterruptedException exception) {
        break;
      }
    }
  }
}
