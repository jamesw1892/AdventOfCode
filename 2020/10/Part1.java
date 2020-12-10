import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class Part1 {

    private static int ans(TreeSet<Integer> joltages) {

        int prev = 0;
        int diff1 = 0;
        int diff3 = 0;

        for (Integer joltage: joltages) {
            switch (joltage - prev) {
                case 1: diff1++; break;
                case 3: diff3++; break;
            }
            prev = joltage;
        }

        // our own joltage adapter
        // prev is maximum so ours is rated 3 higher
        diff3++;

        return diff1 * diff3;
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

        return ans(joltages);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test_1.txt") == 7*5, "Failed test input 1");
        myAssert(solve("input_test_2.txt") == 22*10, "Failed test input 2");

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