import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Part2 {

    private static int getNumQsEveryoneYesGroup(ArrayList<String> lines) {

        HashSet<Character> uniqueQs = new HashSet<>();
        for (char chr: lines.get(0).toCharArray()) {
            uniqueQs.add(chr);
        }

        for (int i = 1; i < lines.size(); i++) {
            HashSet<Character> temp = new HashSet<>();
            for (char chr: lines.get(i).toCharArray()) {
                temp.add(chr);
            }
            uniqueQs.retainAll(temp);
        }

        return uniqueQs.size();
    }

    private static int getNumQsEveryoneYesAllGroups(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<String> linesCurrentGroup = new ArrayList<>();
        int totalNumQs = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                totalNumQs += getNumQsEveryoneYesGroup(linesCurrentGroup);
                linesCurrentGroup.clear();
            } else {
                linesCurrentGroup.add(line);
            }
        }
        scanner.close();
        totalNumQs += getNumQsEveryoneYesGroup(linesCurrentGroup);

        return totalNumQs;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(getNumQsEveryoneYesAllGroups("input_test_2.txt") == 6, "Failed test input 2");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumQsEveryoneYesAllGroups("input.txt"));
        }
    }
}