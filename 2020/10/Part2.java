import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class Part2 {

    private static long ans(Integer[] joltages) {
        /*
        Dynamic programming algorithm, input: sorted array of joltages in ascending order

        opt[i] is the number of arrangements of the first i adapters that always uses adapter i.

        We need the last adapter because it needs to be compatible with our built-in adapter
        which is always 3 more than the maximum so we need to use the maximum adapter (this
        is the last since the input is sorted).

        General case - i >= 3:
        - opt[i] is always at least opt[i-1] since joltages[i] - joltages[i-1] is always <= 3 and
        in this case, there is 1 arrangement of the first i for every arrangement of the first i-1.
        - If joltages[i] - joltages[i-2] <= 3 (it must be >= 2) then another valid arrangement
        is to skip the i-1 th adapter so we add the number of arrangements of the first i-2 adapters.
        - If joltages[i] - joltages[i-3] <= 3 (it must be >= 3) then another valid arrangement is to skip
        the i-1 th and i-2 th adapters so we add the number of arrangements of the first i-3 adapters.

        We also need to deal with the base cases since joltages[i-3] is out of range for i < 3 but there
        are still arrangements as could make any of the first 3 adapters the first if they are <= 3.

        I implement this in a bottom-up way, calculating opt[i] FOR i = 1 TO n and we want opt[n].
        We may need opt[i-1], opt[i-2] and opt[i-3] to calculate opt[i] so we only need constant space
        but I save all values to keep it simple
        */

        // first 3 cases are base cases since for i < 3, joltages[i-3] is out of range.
        // if they are less than 3 then we can include the arrangement where it is the first adapter
        long[] opt = new long[joltages.length];
        opt[0] = 1L;
        opt[1] = opt[0];
        if (joltages[1] <= 3) { // can also miss adapter index 0
            opt[1] += 1L;
        }
        opt[2] = opt[1];
        if (joltages[2] - joltages[0] <= 3) { // can also miss out adapter index 1
            opt[2] += opt[0];
            if (joltages[2] <= 3) { // can also miss adapter indices 0 and 1
                opt[2] += 1L;
            }
        }

        // general case
        for (int i = 3; i < joltages.length; i++) {
            opt[i] = opt[i-1]; // always possible to have i th adapter with i-1 th adapter
            if (joltages[i] - joltages[i-2] <= 3) {
                opt[i] += opt[i-2]; // another valid arrangement is skipping i-1 th adapter
                if (joltages[i] - joltages[i-3] <= 3) {
                    opt[i] += opt[i-3]; // another valid arrangement is skipping i-1 th and i-2 th adapters
                }
            }
        }

        return opt[joltages.length - 1];
    }

    private static long solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

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

        myAssert(solve("input_test_1.txt") == 8L, "Failed test input 1");
        myAssert(solve("input_test_2.txt") == 19208L, "Failed test input 2");

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