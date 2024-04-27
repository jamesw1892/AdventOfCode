import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

class Part2 {
  private static long processLine(String line) {
    return Long.valueOf(line.split(":")[1].replace(" ", ""));
  }

  private static void calculate(String filename) throws IOException {
    long time;
    long distance;
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      time = processLine(reader.readLine());
      distance = processLine(reader.readLine());
    }
    System.out.println(Part1.numWaysToWin(time, distance));
  }

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      calculate(args[0]);
    } else {
      calculate("input.txt");
    }
  }
}
