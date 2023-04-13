package com.example;

import java.util.concurrent.TimeUnit;

public class InfiniteSleepThread extends Thread {

  @Override
  public void run() {
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException exception) {
        break;
      }
    }
  }
}
