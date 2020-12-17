import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {

    private static boolean isValid(int value, ArrayList<Field> fields) {
        for (Field field: fields) {
            if ((field.aMin <= value && value <= field.aMax)
             || (field.bMin <= value && value <= field.bMax)) {
                return true;
            }
        }
        return false;
    }

    private static long solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        Section section = Section.FIELDS;
        ArrayList<Field> fields = new ArrayList<>();

        long sumInvalidNums = 0L;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (section) {
                case FIELDS:
                    if (line.isBlank()) {
                        section = Section.YOUR_TICKET;
                    } else {
                        String[] splitLine = line.split(": ");
                        String[] ranges = splitLine[1].split(" or ");
                        String[] a = ranges[0].split("-");
                        String[] b = ranges[1].split("-");
                        fields.add(new Field(splitLine[0], Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(b[0]), Integer.parseInt(b[1])));
                    }
                    break;
                case YOUR_TICKET:
                    if (line.isBlank()) {
                        section = Section.NEARBY_TICKETS;
                    } else if (!line.equals("your ticket:")) {
                        // ignore for now
                    }
                    break;
                case NEARBY_TICKETS:
                    if (!line.equals("nearby tickets:")) {
                        for (String val: line.split(",")) {
                            int v = Integer.parseInt(val);
                            if (!isValid(v, fields)) {
                                sumInvalidNums += v;
                            }
                        }
                    }
                    break;
                default:
                    Shared.myAssert(false, "Invalid section");
            }
        }
        scanner.close();

        return sumInvalidNums;
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 71L, "Failed test input");

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