import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {
    private static int solve(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        HexGrid grid = new HexGrid();

        // for each line, flip the tile it leads to
        while (scanner.hasNextLine()) {
            grid.flip(scanner.nextLine());
        }
        scanner.close();

        // count the number of black tiles
        return grid.getNumBlackTiles();
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 10, "Failed test input");

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