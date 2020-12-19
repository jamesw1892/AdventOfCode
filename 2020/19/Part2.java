import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part2 {
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

        // we can see that rule `8: 42 | 42 8` matches 1 or more 42's
        // which we can represent in regex with the + operator:
        obj.rules.put(8, new EvaluatedStr("(" + obj.getRegex(42) + "+)", true));

        // we can see that rule ``11: 42 31 | 42 11 31` matches 1 or more 42's
        // followed by the same number of 31's. First I tried 1 or more 42's followed
        // by 1 or more 31's but this matched too many so clearly they have to be the same
        // number. There is no way to represent this in regex directly so I generated long
        // regex myself, assuming there weren't more than 10 of each:
        String p42 = obj.getRegex(42);
        String p31 = obj.getRegex(31);
        String p11 = "(";
        for (int i = 1; i <= 10; i++) {
            p11 += String.format("%s{%d}%s{%d}|", p42, i, p31, i);
        }
        p11 = p11.substring(0, p11.length() - 1) + ")";
        obj.rules.put(11, new EvaluatedStr(p11, true));

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

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 12, "Failed test input");

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