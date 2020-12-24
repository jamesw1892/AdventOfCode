import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part2 {
    private static int solve(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        HexGrid grid = new HexGrid();

        // for each line, flip the tile it leads to
        while (scanner.hasNextLine()) {
            grid.flip(scanner.nextLine());
        }
        scanner.close();

        // change the tiles using the rules for each of 100 days
        grid.days(100);

        // count the number of black tiles after 100 days
        return grid.getNumBlackTiles();
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 2208, "Failed test input");

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