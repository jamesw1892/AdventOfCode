import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

class Part1 {
  static final Map<String, Integer> maxColourCount = Map.of(
    "red"  , 12,
    "green", 13,
    "blue" , 14
  );

  /**
   * A colour count is impossible if it has more than the maximum number of that colour in it
   */
  private static boolean impossibleColourCount(String colourCount) {
    String[] splat = colourCount.split(" ");
    int count = Integer.valueOf(splat[0]);
    String colour = splat[1];
    return count > maxColourCount.get(colour);
  }

  private static boolean possibleGame(String line) {
    return !Arrays.stream(line.split(": ")[1] // Remove Game ID from start of line
                              .split("; "))   // Create stream of each set
                  .flatMap(set -> Arrays.stream(set.split(", "))) // Make each colour count of each set be a separate element in the stream
                  .anyMatch(Part1::impossibleColourCount); // If any are impossible, the game is impossible
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
