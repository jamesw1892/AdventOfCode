import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {

    private static int getNumValidPassports(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        int numValidPassports = 0;
        boolean hasBYR = false;
        boolean hasIYR = false;
        boolean hasEYR = false;
        boolean hasHGT = false;
        boolean hasHCL = false;
        boolean hasECL = false;
        boolean hasPID = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("")) {
                if (hasBYR && hasIYR && hasEYR && hasHGT && hasHCL && hasECL && hasPID) {
                    numValidPassports++;
                }
                hasBYR = false;
                hasIYR = false;
                hasEYR = false;
                hasHGT = false;
                hasHCL = false;
                hasECL = false;
                hasPID = false;
            } else {
                String[] splitLine = line.split(" ");
                for (String kvp: splitLine) {
                    String[] kvpSplit = kvp.split(":");
                    assert kvpSplit.length == 2: "key value pair does not contain exactly one colon";
                    switch (kvpSplit[0]) {
                        case "byr": hasBYR = true; break;
                        case "iyr": hasIYR = true; break;
                        case "eyr": hasEYR = true; break;
                        case "hgt": hasHGT = true; break;
                        case "hcl": hasHCL = true; break;
                        case "ecl": hasECL = true; break;
                        case "pid": hasPID = true; break;
                    }
                }
            }
        }
        scanner.close();
        if (hasBYR && hasIYR && hasEYR && hasHGT && hasHCL && hasECL && hasPID) {
            numValidPassports++;
        }

        return numValidPassports;
    }

    private static void test() throws IOException {
        assert getNumValidPassports("input_test_part1.txt") == 2: "Failed test input";

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumValidPassports("input.txt"));
        }
    }
}