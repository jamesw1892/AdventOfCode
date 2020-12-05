import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Part2 {

    private static int getSeatID(String seatStr) {
        String rowBin = "";
        for (int i = 0; i < 7; i++) {
            rowBin += seatStr.charAt(i) == 'B' ? '1' : '0';
        }
        String colBin = "";
        for (int i = 7; i < 10; i++) {
            colBin += seatStr.charAt(i) == 'R' ? '1' : '0';
        }
        int row = Integer.parseInt(rowBin, 2);
        int col = Integer.parseInt(colBin, 2);

        return 8 * row + col;
    }

    private static HashSet<Integer> getAllSeatIDs(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashSet<Integer> seatIDs = new HashSet<>();

        while (scanner.hasNextLine()) {
            seatIDs.add(getSeatID(scanner.nextLine()));
        }
        scanner.close();

        return seatIDs;
    }

    private static int getMySeatID(String name) throws IOException {
        HashSet<Integer> seatIDs = getAllSeatIDs(name);

        int mySeatID = 0;
        boolean containedLast = false;
        for (int possiblyMySeatID = 0; possiblyMySeatID < 1023; possiblyMySeatID++) {
            if (!seatIDs.contains(possiblyMySeatID)) {
                if (containedLast && seatIDs.contains(possiblyMySeatID + 1)) {
                    mySeatID = possiblyMySeatID;
                }
                containedLast = false;
            } else {
                containedLast = true;
            }
        }

        return mySeatID;
    }

    private static void test() throws IOException {

        int a = getSeatID("FBFBBFFRLR");

        assert a == 357: "Failed getSeatID";

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getMySeatID("input.txt"));
        }
    }
}