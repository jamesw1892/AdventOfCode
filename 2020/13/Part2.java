import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Part2 {

    private static boolean works(HashMap<Long, Long> buses, long n) {
        for (Long key: buses.keySet()) {
            long mod = n % buses.get(key);
            if (key == 0) {
                if (mod != 0) {
                    return false;
                }
            } else {
                if (mod != (buses.get(key) - key)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static long bruteForce(HashMap<Long, Long> buses) {

        // optimisation 1 - don't increment by 1 but by one of the values since it must be divisible by it
        long aKey = buses.keySet().iterator().next();
        long aValue = buses.get(aKey);
        long n = aKey;
        while (n == 0 || !works(buses, n)) {
            n += aValue;
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

    private static long getAns(HashMap<Long, Long> buses) {
        // return bruteForce(buses);
        return minInc(buses);
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

        myAssert(solve("input_test.txt") == 1068781L, "Failed test input");
        myAssert(solve("input_test_1.txt") == 3417L, "Failed test input 1");
        myAssert(solve("input_test_2.txt") == 754018L, "Failed test input 2");
        myAssert(solve("input_test_3.txt") == 779210L, "Failed test input 3");
        myAssert(solve("input_test_4.txt") == 1261476L, "Failed test input 4");
        myAssert(solve("input_test_5.txt") == 1202161486L, "Failed test input 5");

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