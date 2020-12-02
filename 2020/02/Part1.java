import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {

    public static int getNumValid(String name) throws IOException, NumberFormatException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);
        int numValid = 0;
        while (scanner.hasNextLine()) {
            if (isValid(scanner.nextLine())) {
                numValid++;
            }
        }
        scanner.close();
        return numValid;
    }

    private static boolean isValid(String line) throws NumberFormatException {
        String[] splitLine = line.split(": ");
        assert splitLine.length == 2: "Line does not have exactly 1 ': '";
        String policy = splitLine[0];
        String password = splitLine[1];

        String[] splitPolicy = policy.split(" ");
        assert splitPolicy.length == 2: "Policy does not have exactly 1 space";
        String numChars = splitPolicy[0];
        assert splitPolicy[1].length() == 1: "Required char is not a single character";
        char requiredChar = splitPolicy[1].toCharArray()[0];

        String[] splitNumChars = numChars.split("-");
        assert splitNumChars.length == 2: "Num chars does not contains exactly 1 dash";
        int minNum = Integer.parseInt(splitNumChars[0]);
        int maxNum = Integer.parseInt(splitNumChars[1]);

        int count = 0;
        for (char c: password.toCharArray()) {
            if (c == requiredChar) {
                count++;
            }
        }
        
        return minNum <= count && count <= maxNum;
    }

    private static void test() throws IOException, NumberFormatException {
        assert getNumValid("input_test.txt") == 2: "Failed test input";

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException, NumberFormatException {
        System.out.println(getNumValid("input.txt"));
        // test();
    }
}