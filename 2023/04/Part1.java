import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.stream.Stream;

class Part1 {
  private static long numWins(String card) {
    String[] nums = card.split(": ")[1].split(" \\| ");
    HashSet<String> winningNums = new HashSet<>();
    for (String num: nums[0].split(" ")) {
      num = num.strip();
      if (!num.isEmpty()) {
        winningNums.add(num);
      }
    }
    return Arrays.stream(nums[1].split(" "))
          .map(String::strip)
          .filter(Predicate.not(String::isEmpty))
          .filter(num -> winningNums.contains(num))
          .count();
  }

  private static long cardPoints(String card) {
    return (long) Math.pow(2, numWins(card) - 1);
  }

  private static void calculate(String filename) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      System.out.println(lines.mapToLong(Part1::cardPoints)
                              .sum());
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
