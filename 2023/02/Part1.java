import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

class Part1 {
  static final int MAX_RED = 12;
  static final int MAX_GREEN = 13;
  static final int MAX_BLUE = 14;

  /**
   * A colour count is impossible if it has more than the maximum number of that colour in it
   */
  private static boolean impossibleColourCount(String colourCount) {
    String[] splat = colourCount.split(" ");
    int count = Integer.valueOf(splat[0]);
    String colour = splat[1];
    switch (colour) {
      case "red"  : return count > MAX_RED;
      case "green": return count > MAX_GREEN;
      case "blue" : return count > MAX_BLUE;
      default: throw new IllegalArgumentException("Invalid colour");
    }
  }

  private static boolean impossibleSet(String set) {
    return Arrays.stream(set.split(", ")) // Create stream of each colour count
                 .anyMatch(Part1::impossibleColourCount); // If any are impossible, the set is impossible
  }

  private static boolean possibleGame(String line) {
    return !Arrays.stream(line.split(": ")[1] // Remove Game ID from start of line
                              .split("; "))   // Create stream of each set
                  .anyMatch(Part1::impossibleSet); // If any are impossible, the game is impossible
  }

  private static void calculate(String filename) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      System.out.println(lines.filter(Part1::possibleGame) // Only keep games that are possible
                              .mapToInt(line -> Integer.valueOf(line.split(": ")[0].split("Game ")[1])) // Convert line to int Game ID
                              .sum()); // Add up the game IDs
    }
  }

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      calculate(args[0]);
    } else {
      calculate("input.txt");
    }
  }
}
