import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {
    private static int ans(int expected, ArrayList<Integer> times) {

        int minID = times.get(0);
        int minTime = ((expected / minID) + 1) * minID;

        int mod;
        int ID;
        for (int index = 1; index < times.size(); index++) {
            ID = times.get(index);
            mod = ((expected / ID) + 1) * ID;
            if (mod < minTime) {
                minID = ID;
                minTime = mod;
            }
        }

        return minID * (minTime - expected);
    }

    private static int solve(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        int expected = Integer.parseInt(scanner.nextLine());
        ArrayList<Integer> times = new ArrayList<>();
        for (String timeStr: scanner.nextLine().split(",")) {
            if (!timeStr.equals("x")) {
                times.add(Integer.parseInt(timeStr));
            }
        }
        scanner.close();

        return ans(expected, times);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 295, "Failed test input");

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
