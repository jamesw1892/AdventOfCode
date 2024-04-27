import static java.util.function.Predicate.not;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Part1 {

  /**
   * When given a string of (potentially multiple) space separated numbers (winning or not), return a stream of them
   */
  private static Stream<String> processNums(String nums) {
    return Arrays.stream(nums.split(" "))
        .map(String::strip)
        .filter(not(String::isEmpty));
  }

  /**
   * Return the number of our numbers that are also winning numbers
   */
  private static long numWins(String card) {
    String[] nums = card.split(": ")[1].split(" \\| ");
    Set<String> winningNums = processNums(nums[0]).collect(Collectors.toSet());
    return processNums(nums[1]).filter(num -> winningNums.contains(num)).count();
  }

  /**
   * The number of points for a card is 2 to the power of the number of winning numbers minus 1
   */
  private static long cardPoints(String card) {
    return (long) Math.pow(2, numWins(card) - 1);
  }

  private static void calculate(String filename) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      System.out.println(lines.mapToLong(Part1::cardPoints).sum()); // Sum of points for each card
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
