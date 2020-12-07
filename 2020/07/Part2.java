import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Part2 {

    private static int recurse(String current, HashMap<String, HashMap<String, Integer>> bagsContains) {
        int total = 0;
        if (bagsContains.containsKey(current)) {
            HashMap<String, Integer> map = bagsContains.get(current);
            for (String bag: map.keySet()) {
                total += (recurse(bag, bagsContains)+1) * map.get(bag);
            }
        }
        return total;
    }

    private static int getNumBags(String name) throws IOException {
        return recurse("shiny gold", parseInput(name));
    }

    private static HashMap<String, HashMap<String, Integer>> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<String, HashMap<String, Integer>> bagContains = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" bags contain ");
            myAssert(splitLine.length == 2, "Line should contain ' bags contain '");
            String outer = splitLine[0];
            String inner = splitLine[1];
            HashMap<String, Integer> innerBags = new HashMap<>();
            if (!inner.equals("no other bags.")) {
                String[] splitInner = inner.split(", ");
                for (String innerBag: splitInner) {
                    String[] splitI = innerBag.split(" bag");
                    String[] splitInnerBag = splitI[0].split(" ", 2);
                    int count = Integer.parseInt(splitInnerBag[0]);
                    String bag = splitInnerBag[1];
                    innerBags.put(bag, count);
                }
            }
            bagContains.put(outer, innerBags);
        }
        scanner.close();

        return bagContains;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(getNumBags("input_test.txt") == 32, "Failed test input");
        myAssert(getNumBags("input_test_part_2.txt") == 126, "Failed test input part 2");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumBags("input.txt"));
        }
    }
}