import static java.util.function.Predicate.not;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.IntStream;

class Part1 {
  private static long[] processLine(String line) {
    return Arrays.stream(line.split(":")[1].split(" "))
        .map(String::strip)
        .filter(not(String::isEmpty))
        .mapToLong(Long::valueOf)
        .toArray();
  }

  static long numWaysToWin(long time, long distance) {
    /**
     * The way to get the furthest is to hold the button down for exactly half
     * the time. This can be calculated by finding the maximum point of the
     * graph y = x * (time - x) where time is the maximum time you have, x is
     * the time you hold the button down for and y is how far you go. This is
     * because distance = speed * time, your speed is the time you hold the
     * button down for and your time travelling is the total time subtract the
     * time you hold the button down for.
     * 
     * To find how many options we have when we have to pick a whole number of
     * miliseconds (x is an integer), we can solve the equation above for x
     * when y = the distance to beat. We do this with the quadratic formula
     * where a = 1, b = -time and c = distance.
     */
    double sqrtDiscriminant = Math.sqrt(time * time - 4 * distance);

    // Round to nearest integer since we must hold the button for a whole
    // number of miliseconds
    long minTimeToPress = (long) Math.ceil((time - sqrtDiscriminant) / 2);
    long maxTimeToPress = (long) Math.floor((time + sqrtDiscriminant) / 2);

    // Must beat the existing record so if equals it then can't hold down for
    // that number
    long distanceMinTimeToPress = minTimeToPress * (time - minTimeToPress);
    long distanceMaxTimeToPress = maxTimeToPress * (time - maxTimeToPress);
    if (distanceMinTimeToPress == distance) minTimeToPress += 1;
    if (distanceMaxTimeToPress == distance) maxTimeToPress -= 1;

    // Count the number of milliseconds that would beat the record which is
    // the number of numbers from min to max inclusive
    return maxTimeToPress - minTimeToPress + 1;
  }

  private static void calculate(String filename) throws IOException {
    long[] times;
    long[] distances;
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      times = processLine(reader.readLine());
      distances = processLine(reader.readLine());
    }
    System.out.println(IntStream.range(0, times.length)
        .mapToLong(index -> numWaysToWin(times[index], distances[index]))
        .reduce(1, Math::multiplyExact));
  }

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      calculate(args[0]);
    } else {
      calculate("input.txt");
    }
  }
}
