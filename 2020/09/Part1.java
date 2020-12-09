import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {

    private static boolean isSum(int num, ArrayList<Integer> previousNumbers) {
        for (Integer n1: previousNumbers) {
            if (2 * n1 != num) {
                for (Integer n2: previousNumbers) {
                    if (n1 + n2 == num) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Integer solve(String name, int sizePreamble) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<Integer> previousNumbers = new ArrayList<>();

        int num;
        while (scanner.hasNextLine()) {
            num = Integer.parseInt(scanner.nextLine());
            if (previousNumbers.size() >= sizePreamble) {
                if (!isSum(num, previousNumbers)) {
                    return num;
                }
                previousNumbers.remove(0);
            }
            previousNumbers.add(num);
        }
        scanner.close();
        
        return null;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 1; i <= 25; i++) a.add(i);
        myAssert(isSum(26, a), "Failed isSum 1 with 26");
        myAssert(isSum(49, a), "Failed isSum 1 with 49");
        myAssert(!isSum(100, a), "Failed isSum 1 with 100");
        myAssert(!isSum(50, a), "Failed isSum 1 with 50");
        a.remove(19); // remove the value 20 at index 19
        a.add(45);
        myAssert(isSum(26, a), "Failed isSum 2 with 26");
        myAssert(!isSum(65, a), "Failed isSum 2 with 65");
        myAssert(isSum(64, a), "Failed isSum 2 with 64");
        myAssert(isSum(66, a), "Failed isSum 2 with 66");

        myAssert(solve("input_test.txt", 5) == 127, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("input.txt", 25));
        }
    }
}