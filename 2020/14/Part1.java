import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Part1 {

    private static long sum(HashMap<Long, Long> memory) {
        long total = 0L;
        for (Long key: memory.keySet()) {
            total += memory.get(key);
        }
        return total;
    }

    private static void setValue(HashMap<Long, Long> memory, long memAdd, long value, String mask) {

        String orMaskS = "";     // 1s are set to 1, 0s stay
        String andMaskS = "";    // 0s are set to 0, 1s stay

        for (char chr: mask.toCharArray()) {
            switch (chr) {
                case 'X':
                    orMaskS += '0';
                    andMaskS += '1';
                    break;
                default:
                    orMaskS += chr;
                    andMaskS += chr;
            }
        }

        long orMask = Long.parseLong(orMaskS, 2);
        long andMask = Long.parseLong(andMaskS, 2);

        long out = (value & andMask) | orMask;

        memory.put(memAdd, out);
    }

    private static long solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<Long, Long> nonZeroMemory = new HashMap<>();
        String mask = "";
        long memAdd;
        long value;
        String[] splitLine;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("mask")) {
                mask = line.split("mask = ")[1];
            } else {
                splitLine = line.split("mem\\[")[1].split("\\] = ");
                memAdd = Long.parseLong(splitLine[0]);
                value = Long.parseLong(splitLine[1]);
                setValue(nonZeroMemory, memAdd, value, mask);
            }
        }
        scanner.close();

        return sum(nonZeroMemory);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test_part_1.txt") == 165L, "Failed test input");

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