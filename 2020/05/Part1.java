import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {

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

    private static int getHighestSeatID(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        int highestSeatID = 0;

        while (scanner.hasNextLine()) {
            int seatID = getSeatID(scanner.nextLine());
            if (seatID > highestSeatID) {
                highestSeatID = seatID;
            }
        }
        scanner.close();

        return highestSeatID;
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
            System.out.println(getHighestSeatID("input.txt"));
        }
    }
}