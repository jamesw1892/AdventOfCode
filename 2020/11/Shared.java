import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Shared {
    public static ArrayList<ArrayList<Seat>> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<ArrayList<Seat>> grid = new ArrayList<>();
        
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
            grid.add(inner);
        }
        scanner.close();

        return grid;
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }
}