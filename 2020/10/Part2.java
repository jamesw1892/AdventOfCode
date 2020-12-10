import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Part2 {

    private static int min(int a, int b) {
        if (a < b) {
            return a;
        }
        return b;
    }

    private static int recurseIndex(Integer[] joltages, int startIndex, int prev) {
        // base case
        if (startIndex == joltages.length) return 1;

        ArrayList<Integer> candidates = new ArrayList<>();
        int m = min(3, joltages.length - startIndex);
        for (int i = 0; i < m; i++) {
            int joltage = joltages[startIndex + i];
            if (joltage <= prev + 3) {
                candidates.add(joltage);
            }
        }

        int total = 0;
        for (int i = 0; i < candidates.size(); i++) {
            total += recurseIndex(joltages, startIndex + i + 1, candidates.get(i));
        }
        return total;
    }

    private static int ans(Integer[] joltages) {
        return recurseIndex(joltages, 0, 0);
    }

    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        // can use a set because even if there are repeats,
        // they would be immediately after each other and have
        // a difference of 0 jolts which doesn't affect the
        // final answer
        TreeSet<Integer> joltages = new TreeSet<>();

        while (scanner.hasNextLine()) {
            joltages.add(Integer.parseInt(scanner.nextLine()));
        }
        scanner.close();

        Integer[] joltagesArray = joltages.toArray(new Integer[0]);

        return ans(joltagesArray);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test_1.txt") == 8, "Failed test input 1");
        myAssert(solve("input_test_2.txt") == 19208, "Failed test input 2");

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