import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Part1 {

    private static int getNumQsAnyoneYesGroup(ArrayList<String> lines) {

        HashSet<Character> uniqueQs = new HashSet<>();

        for (String line: lines) {
            for (char chr: line.toCharArray()) {
                uniqueQs.add(chr);
            }
        }

        return uniqueQs.size();
    }

    private static int getNumQsAnyoneYesAllGroups(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<String> linesCurrentGroup = new ArrayList<>();
        int totalNumQs = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                totalNumQs += getNumQsAnyoneYesGroup(linesCurrentGroup);
                linesCurrentGroup.clear();
            } else {
                linesCurrentGroup.add(line);
            }
        }
        scanner.close();
        totalNumQs += getNumQsAnyoneYesGroup(linesCurrentGroup);

        return totalNumQs;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(getNumQsAnyoneYesAllGroups("input_test_1.txt") == 6, "Failed test input 1");
        myAssert(getNumQsAnyoneYesAllGroups("input_test_2.txt") == 11, "Failed test input 2");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumQsAnyoneYesAllGroups("input.txt"));
        }
    }
}