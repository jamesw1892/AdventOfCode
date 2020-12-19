import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {
    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        boolean secondState = false;

        ArrayList<String> receivedMessages = new ArrayList<>();
        TreeMap<Integer, EvaluatedStr> rules = new TreeMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                secondState = true;
            } else if (secondState) {
                receivedMessages.add(line);
            } else {
                String[] splitLine = line.split(": ");
                rules.put(Integer.parseInt(splitLine[0]), new EvaluatedStr(splitLine[1], false));
            }
        }
        scanner.close();

        Shared obj = new Shared(rules);
        Pattern pattern = Pattern.compile(obj.getRegex(0));

        int numMatches = 0;
        for (String message: receivedMessages) {
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                numMatches++;
            }
        }

        return numMatches;
    }

    private static TreeMap<Integer, EvaluatedStr> parseRules(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        TreeMap<Integer, EvaluatedStr> rules = new TreeMap<>();

        while (scanner.hasNextLine()) {
            String[] splitLine = scanner.nextLine().split(": ");
            rules.put(Integer.parseInt(splitLine[0]), new EvaluatedStr(splitLine[1], false));
        }
        scanner.close();

        return rules;
    }

    private static void test() throws IOException {

        Shared obj;

        obj = new Shared(parseRules("input_test_1.txt"));
        Shared.myAssert(obj.getRegex(0).equals("(a(ab|ba))"), "Failed test input 1");

        obj = new Shared(parseRules("input_test_2.txt"));
        Shared.myAssert(obj.getRegex(0).equals("(a((aa|bb)(ab|ba)|(ab|ba)(aa|bb))b)"), "Failed test input 2");

        Shared.myAssert(solve("input_test_3.txt") == 2, "Failed test input 3");

        Shared.myAssert(solve("input_test.txt") == 3, "Failed test input");

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