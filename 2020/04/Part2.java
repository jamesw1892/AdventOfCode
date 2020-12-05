import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part2 {

    private static boolean checkBYR(String value) {
        if (value.length() != 4) return false;

        int val;
        try {
            val = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (val < 1920) return false;
        if (val > 2002) return false;

        return true;
    }

    private static boolean checkIYR(String value) {
        if (value.length() != 4) return false;

        int val;
        try {
            val = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (val < 2010) return false;
        if (val > 2020) return false;

        return true;
    }

    private static boolean checkEYR(String value) {
        if (value.length() != 4) return false;

        int val;
        try {
            val = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (val < 2020) return false;
        if (val > 2030) return false;

        return true;
    }

    private static boolean checkHGT(String value) {

        int val;
        if (value.endsWith("in")) {
            try {
                val = Integer.valueOf(value.split("in")[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (val < 59) return false;
            if (val > 76) return false;

            return true;
        } else if (value.endsWith("cm")) {
            try {
                val = Integer.valueOf(value.split("cm")[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (val < 150) return false;
            if (val > 193) return false;

            return true;
        }
        return false;
    }

    private static boolean checkHCL(String value) {
        if (value.charAt(0) != '#') return false;
        for (int i = 1; i < 7; i++) {
            char ch = value.charAt(i);
            if ((!Character.isDigit(ch)) && ch != 'a' && ch != 'b' && ch != 'c' && ch != 'd' && ch != 'e' && ch != 'f') return false;
        }
        return true;
    }

    private static boolean checkECL(String value) {
        switch (value) {
            case "amb":
            case "blu":
            case "brn":
            case "gry":
            case "grn":
            case "hzl":
            case "oth":
                break;
            default: return false;
        }
        return true;
    }

    private static boolean checkPID(String value) {
        if (value.length() != 9) return false;
        for (char chr: value.toCharArray()) {
            if (!Character.isDigit(chr)) return false;
        }
        return true;
    }

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
                    myAssert(kvpSplit.length == 2, "key value pair does not contain exactly one colon");
                    switch (kvpSplit[0]) {
                        case "byr": hasBYR = checkBYR(kvpSplit[1]); break;
                        case "iyr": hasIYR = checkIYR(kvpSplit[1]); break;
                        case "eyr": hasEYR = checkEYR(kvpSplit[1]); break;
                        case "hgt": hasHGT = checkHGT(kvpSplit[1]); break;
                        case "hcl": hasHCL = checkHCL(kvpSplit[1]); break;
                        case "ecl": hasECL = checkECL(kvpSplit[1]); break;
                        case "pid": hasPID = checkPID(kvpSplit[1]); break;
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

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(checkBYR("2002"), "BYR check 1 failed");
        myAssert(!checkBYR("2003"), "BYR check 2 failed");

        myAssert(checkHGT("60in"), "HGT check 1 failed");
        myAssert(checkHGT("190cm"), "HGT check 2 failed");
        myAssert(!checkHGT("190in"), "HGT check 3 failed");
        myAssert(!checkHGT("190"), "HGT check 4 failed");

        myAssert(checkHCL("#123abc"), "HCL check 1 failed");
        myAssert(!checkHCL("#123abz"), "HCL check 2 failed");
        myAssert(!checkHCL("123abc"), "HCL check 3 failed");

        myAssert(checkECL("brn"), "ECL check 1 failed");
        myAssert(!checkECL("wat"), "ECL check 2 failed");

        myAssert(checkPID("000000001"), "PID check 1 failed");
        myAssert(!checkPID("0123456789"), "PID check 2 failed");

        myAssert(getNumValidPassports("input_test_part2.txt") == 4, "Failed test input");

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