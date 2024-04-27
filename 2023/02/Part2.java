import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

class Part2 {
  private static long maxColour(String game, String colour) {
    return Arrays.stream(game.split("; "))                        // Create a stream of each set
                 .flatMap(set -> Arrays.stream(set.split(", ")))  // Make each colourCount in each set a separate element of the stream
                 .filter(   colourCount ->              colourCount.split(" ")[1].equals(colour)) // Only keep those with the colour we're interested in
                 .mapToLong(colourCount -> Long.valueOf(colourCount.split(" ")[0])) // Convert the colourCount to the count
                 .max() // Find the maximum count of that colour in the stream
                 .orElseThrow(); // It returns an optional in case the stream is empty but it shouldn't be
  }

  /**
   * The power of a game is the product of the maximum number of each colour in the game
   */
  private static Long gamePower(String game) {
    return Stream.of(maxColour(game, "red"),
                     maxColour(game, "green"),
                     maxColour(game, "blue"))
                 .reduce(1L, Math::multiplyExact);
  }

  private static void calculate(String filename) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      System.out.println(lines.map(line -> line.split(": ")[1]) // Remove Game ID from start of line
                              .mapToLong(Part2::gamePower) // Convert the rest of each line to the power of the game
                              .sum()); // Add up the powers of the games
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
