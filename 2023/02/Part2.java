import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Part2 {

  /**
   * The power of a game is the product of the minimum number of each colour in the game
   */
  private static Long gamePower(String game) {
    Long redMax = 0L;
    Long greenMax = 0L;
    Long blueMax = 0L;
    for (String set: game.split("; ")) {
      for (String colourCount: set.split(", ")) {
        String[] splat = colourCount.split(" ");
        long count = Long.valueOf(splat[0]);
        String colour = splat[1];
        switch (colour) {
          case "red"  : if (count >   redMax)   redMax = count; break;
          case "green": if (count > greenMax) greenMax = count; break;
          case "blue" : if (count >  blueMax)  blueMax = count; break;
        }
      }
    }
    return redMax * greenMax * blueMax;
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
