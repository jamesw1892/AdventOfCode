import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

class Part2 {

  /**
   * Calculate the total number of cards of this number and adjust the HashMap
   * for future card numbers.
   */
  private static Long calcNumCopies(String line, HashMap<Integer, Long> copies) {
    int cardNum = Integer.valueOf(line.split(":")[0].split("Card ")[1].strip());
    long numWins = Part1.numWins(line);

    // Fetch the number of copies from the HashMap and add the original
    long copiesOfThisCard = copies.getOrDefault(cardNum, 0L) + 1;

    // Add `copiesOfThisCard` extra copies to the `numWins` cards with numbers after this one
    for (int newCardNum = cardNum + 1; newCardNum <= cardNum + numWins; newCardNum++) {
      copies.put(newCardNum, copies.getOrDefault(newCardNum, 0L) + copiesOfThisCard);
    }
    return copiesOfThisCard;
  }

  private static void calculate(String filename) throws IOException {
    // Notice that since this card only affects the number of copies of FUTURE cards,
    // we can still do this in one iteration.

    // Store in this HashMap the number of copies of future cards (not including the card itself)
    HashMap<Integer, Long> copies = new HashMap<>();

    // Read the file line by line where each line is a card, run the function
    // on it to calculate the number of copies (including the card itself) and
    // adjust the copies of future cards. Then sum up these numbers over all cards
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      System.out.println(lines.mapToLong(line -> calcNumCopies(line, copies)).sum());
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
