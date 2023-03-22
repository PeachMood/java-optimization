package task1;

import java.util.List;

import org.openjdk.jol.info.GraphLayout;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MemoryTest {
  public static class Test {
    public int[] array = new int[10000];
  }

  public static void main(String[] args) throws IOException {
    System.out.println("\nTotal object size: " + GraphLayout.parseInstance(new Test()).totalSize() + "\n");

    FileWriter data = new FileWriter(new File("./task1/data.csv"));
    data.write("Max, Total, Free, Used, Object\n");

    long used = 0;
    List<Test> list = new ArrayList<>(20000);

    while (true) {
      try {

        list.add(new Test());

        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();

        data.append(max + ", ");
        data.append(total + ", ");
        data.append(free + ", ");
        data.append(total - free + ", ");
        data.append((total - free) - used + "\n");

        used = total - free;

      } catch (Exception exception) {
        data.close();
      }
    }
  }
}
