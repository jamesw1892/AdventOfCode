import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part2 {

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
        String charIndices = splitPolicy[0];
        assert splitPolicy[1].length() == 1: "Required char is not a single character";
        char requiredChar = splitPolicy[1].toCharArray()[0];

        String[] splitCharIndices = charIndices.split("-");
        assert splitCharIndices.length == 2: "Num chars does not contains exactly 1 dash";
        int index1 = Integer.parseInt(splitCharIndices[0]);
        int index2 = Integer.parseInt(splitCharIndices[1]);

        boolean index1Equal = password.charAt(index1 - 1) == requiredChar;
        boolean index2Equal = password.charAt(index2 - 1) == requiredChar;

        return index1Equal != index2Equal;
    }

    private static void test() throws IOException, NumberFormatException {
        assert getNumValid("input_test.txt") == 1: "Failed test input";

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException, NumberFormatException {
        System.out.println(getNumValid("input.txt"));
        // test();
    }
}