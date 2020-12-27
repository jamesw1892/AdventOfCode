import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Part2 {

    /**
     * Check whether n is a valid solution with buses so for each bus:
     * it's 
     */
    private static boolean works(HashMap<Long, Long> buses, long n) {
        for (Long key: buses.keySet()) {
            if ((n + key) % buses.get(key) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find by brute force by trying all possible values but it is slightly
     * optimised to only check the ones that the largest ID works for, this
     * means we increment by the largest ID so check the fewest possible values.
     * This is still too slow.
     */
    private static long bruteForce(HashMap<Long, Long> buses) {

        // find largest value (bus ID)
        Long maxValue = null;
        long maxValuedKey = 0L;
        for (Long key: buses.keySet()) {
            Long value = buses.get(key);
            if (maxValue == null || maxValue < value) {
                maxValue = value;
                maxValuedKey = key;
            }
        }

        long n = maxValue - maxValuedKey;
        while (n <= 0 || !works(buses, n)) {
            n += maxValue;
        }
        return n;
    }

    private static boolean allEqual(long[] x) {
        long value = x[0];
        for (int index = 1; index < x.length; index++) {
            if (x[index] != value) {
                return false;
            }
        }
        return true;
    }

    private static int getMinIndex(long[] x) {
        int minIndex = 0;
        long minValue = x[0];

        for (int index = 1; index < x.length; index++) {
            long value = x[index];
            if (value < minValue) {
                minIndex = index;
                minValue = value;
            }
        }

        return minIndex;
    }

    /**
     * Increase smallest value by the bus ID until all equal. Think this is
     * actually slower than the optimised brute force
     */
    private static long minInc(HashMap<Long, Long> buses) {
        long[] x = new long[buses.size()];
        long[] values = new long[buses.size()];

        // populate arrays with x starting at offsets, not the numbers themselves
        int index = 0;
        for (Long key: buses.keySet()) {
            x[index] = buses.get(key) - key;
            values[index] = buses.get(key);
            index++;
        }

        // repeatedly increase the smallest value by its multiple until all equal
        while (!allEqual(x)) {
            int minIndex = getMinIndex(x);
            x[minIndex] += values[minIndex];
        }

        return x[0];
    }

    /**
     * Solve the chinese remainder theorem problem by sieving. The divisors must be pairwise coprime
     */
    private static long ChineseRemainderTheorem(TreeMap<Long, Long> map) {

        Entry<Long, Long> entry = map.pollLastEntry();
        long productNs = entry.getKey();
        long x = entry.getValue();
        while (!map.isEmpty()) {
            while (x % map.lastKey() != map.lastEntry().getValue()) {
                x += productNs;
            }
            productNs *= map.pollLastEntry().getKey();
        }

        return x;
    }

    /**
     * Convert this problem into the chinese remainder theorem problem
     * and solve it
     */
    private static long CRT(HashMap<Long, Long> buses) {

        TreeMap<Long, Long> map = new TreeMap<>();

        for (Long key: buses.keySet()) {

            long n = buses.get(key);
            key %= n; // make sure indices are smaller than or equal to bus IDs

            map.put(n, (n - key) % n); // mod n so key=0 stays as 0 rather than n
        }

        return ChineseRemainderTheorem(map);
    }

    private static long getAns(HashMap<Long, Long> buses) {

        // return bruteForce(buses);
        // return minInc(buses);
        return CRT(buses);
    }

    private static HashMap<Long, Long> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        scanner.nextLine(); // skip first line of earliest timestamp
        String line = scanner.nextLine();
        scanner.close();

        HashMap<Long, Long> buses = new HashMap<>();

        long counter = 0L;
        for (String busTime: line.split(",")) {
            if (!busTime.equals("x")) {
                buses.put(counter, Long.parseLong(busTime));
            }
            counter++;
        }

        return buses;
    }

    private static long solve(String name) throws IOException {
        return getAns(parseInput(name));
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        // test works method
        HashMap<Long, Long> buses = new HashMap<>();
        buses.put(0L, 2L);
        buses.put(1L, 3L);
        buses.put(2L, 5L);
        myAssert(works(buses, 8), "Works doesn't work 1");
        myAssert(!works(buses, 5), "Works doesn't work 2");
        myAssert(!works(buses, 7), "Works doesn't work 3");
        myAssert(!works(buses, 9), "Works doesn't work 4");
        
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