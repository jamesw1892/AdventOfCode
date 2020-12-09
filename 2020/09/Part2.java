import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part2 {

    private static boolean isSum(long num, ArrayList<Long> previousNumbers) {
        for (Long n1: previousNumbers) {
            if (2 * n1 != num) {
                for (Long n2: previousNumbers) {
                    if (n1 + n2 == num) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static long sum(int startIndex, int endIndex, ArrayList<Long> array) {
        long total = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            total += array.get(i);
        }
        return total;
    }

    private static long sumOfMinAndMax(int startIndex, int endIndex, ArrayList<Long> array) {
        long min = array.get(startIndex);
        long max = array.get(startIndex);
        for (int i = startIndex + 1; i <= endIndex; i++) {
            long current = array.get(i);
            if (current < min) {
                min = current;
            }
            if (current > max) {
                max = current;
            }
        }
        return min + max;
    }

    private static Long solve(String name, int sizePreamble) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<Long> allNums = new ArrayList<>();

        while (scanner.hasNextLine()) {
            allNums.add(Long.parseLong(scanner.nextLine()));
            
        }
        scanner.close();

        Long numToFind = getSmallestFailure(allNums, sizePreamble);
        myAssert(numToFind != null, "Must exist a smallest failure");

        for (int startIndex = 0; startIndex < allNums.size() - 1; startIndex++) {
            for (int endIndex = startIndex + 1; endIndex < allNums.size(); endIndex++) {
                if (sum(startIndex, endIndex, allNums) == numToFind) {
                    return sumOfMinAndMax(startIndex, endIndex, allNums);
                }
            }
        }
        return null;
    }

    private static Long getSmallestFailure(ArrayList<Long> allNums, int sizePreamble) throws IOException {

        ArrayList<Long> previousNumbers = new ArrayList<>();

        for (Long num: allNums) {
            if (previousNumbers.size() >= sizePreamble) {
                if (!isSum(num, previousNumbers)) {
                    return num;
                }
                previousNumbers.remove(0);
            }
            previousNumbers.add(num);
        }

        return null;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        ArrayList<Long> a = new ArrayList<>();
        for (long i = 1; i <= 25; i++) a.add(i);
        myAssert(isSum(26L, a), "Failed isSum 1 with 26");
        myAssert(isSum(49L, a), "Failed isSum 1 with 49");
        myAssert(!isSum(100L, a), "Failed isSum 1 with 100");
        myAssert(!isSum(50L, a), "Failed isSum 1 with 50");
        a.remove(19); // remove the value 20 at index 19
        a.add(45L);
        myAssert(isSum(26L, a), "Failed isSum 2 with 26");
        myAssert(!isSum(65L, a), "Failed isSum 2 with 65");
        myAssert(isSum(64L, a), "Failed isSum 2 with 64");
        myAssert(isSum(66L, a), "Failed isSum 2 with 66");

        myAssert(solve("input_test.txt", 5) == 15+47, "Failed test input");

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