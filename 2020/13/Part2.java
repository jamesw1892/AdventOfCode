import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Part2 {

    /**
     * Solve the chinese remainder theorem problem by sieving. The divisors must be pairwise coprime.
     * Returns x such that for each n and a pair, x == a (mod n) and 0 <= x < N where N is the product of all n's.
     * @param buses Map where the keys are the divisors (n's) and the values are the corresponding remainders (a's)
     */
    private static long CRT(TreeMap<Long, Long> buses) {

        /**
         * The sieving method finds the solution to the first two pairs of n's and a's
         * then we know the answer must be this plus a multiple of n_1 * n_2. Then we
         * incorporate the 3rd pair and solve and then add multiples of n_1 * n_2 * n_3, etc.
         * 
         * It is more efficient when choosing the largest n's first so that is why we use
         * a tree map and get the last entry
         */

        // remove and return the entry with largest n
        Entry<Long, Long> entry = buses.pollLastEntry();

        // this contains the running product of the n's. It starts as the first n.
        long productNs = entry.getKey();

        /**
         * the solution to the first few pairs, as the outer while loop iterates,
         * this becomes the solution to more and more pairs until at the end it
         * will be the final solution
         */
        long x = entry.getValue();

        while (!buses.isEmpty()) { // while we haven't solved all pairs yet

            while (x % buses.lastKey() != buses.lastEntry().getValue()) { // this is the success condition for the current sub-problem
                x += productNs; // if not solved, we add the product of the n's until it is
            }
            productNs *= buses.pollLastEntry().getKey(); // remove the entry with largest n and add this to the running product
        }

        return x;
    }

    /**
     * Parse the input and convert the problem to the chinese remainder theorem
     * so we can solve it efficiently
     */
    private static TreeMap<Long, Long> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        scanner.nextLine(); // skip first line of earliest timestamp
        String line = scanner.nextLine();
        scanner.close();

        // the chinese remainder theorem works faster if it is done in
        // descending order of n's so we use a tree map to sort them
        TreeMap<Long, Long> buses = new TreeMap<>();

        long counter = 0L; // this is the offset by the time
        for (String busTime: line.split(",")) {
            if (!busTime.equals("x")) {

                // n is the bus ID - the time interval between arrivals
                long n = Long.parseLong(busTime);

                /**
                 * a is the remainder which is n - counter. However, we also
                 * need to mod n first because it could be greater than n, then
                 * we subtract and finally we need to mod again because if
                 * (counter % n) is 0, a should not be n but 0.
                 */
                long a = (n - (counter % n)) % n;

                buses.put(n, a);
            }
            counter++;
        }

        return buses;
    }

    private static long solve(String name) throws IOException {
        return CRT(parseInput(name));
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 1068781L, "Failed test input");
        myAssert(solve("input_test_1.txt") == 3417L, "Failed test input 1");
        myAssert(solve("input_test_2.txt") == 754018L, "Failed test input 2");
        myAssert(solve("input_test_3.txt") == 779210L, "Failed test input 3");
        myAssert(solve("input_test_4.txt") == 1261476L, "Failed test input 4");
        myAssert(solve("input_test_5.txt") == 1202161486L, "Failed test input 5");
        myAssert(solve("input_test_6.txt") == 8L, "Failed test input 6"); // own test with only 2, 3 and 5

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("input.txt"));
        }
    }
}