package com.example;

import java.util.concurrent.TimeUnit;

public class ShortSleepThread extends Thread {

  private void sweetSleep() throws InterruptedException {
    TimeUnit.SECONDS.sleep(1);
    secondSleep();
  }

  private void secondSleep() throws InterruptedException {
    TimeUnit.SECONDS.sleep(2);
    thirdSleep();
  }

  private void thirdSleep() throws InterruptedException {
    TimeUnit.SECONDS.sleep(3);
  }

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(1);
        sweetSleep();
      } catch (InterruptedException exception) {
        break;
      }
    }
  }
}
