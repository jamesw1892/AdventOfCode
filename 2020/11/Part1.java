import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {

    private static int ans(GridPart1 grid) {

        while (grid.applyRules());

        return grid.getNumOccupied();
    }

    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<ArrayList<Seat>> g = new ArrayList<>();
        
        while (scanner.hasNextLine()) {
            ArrayList<Seat> inner = new ArrayList<>();
            for (char chr: scanner.nextLine().toCharArray()) {
                switch (chr) {
                    case '.': inner.add(Seat.FLOOR); break;
                    case 'L': inner.add(Seat.EMPTY); break;
                    case '#': inner.add(Seat.OCCUPIED); break;
                    default: myAssert(false, "Invalid seat");
                }
            }
            g.add(inner);
        }
        scanner.close();

        return ans(new GridPart1(g));
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 37, "Failed test input");

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