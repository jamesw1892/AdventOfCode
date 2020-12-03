import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part2 {

    private static Grid parse(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<ArrayList<Boolean>> grid = new ArrayList<>();

        while (scanner.hasNextLine()) {
            ArrayList<Boolean> line = new ArrayList<>();
            for (char chr: scanner.nextLine().toCharArray()) {
                if (chr == '#') {
                    line.add(true);
                } else if (chr == '.') {
                    line.add(false);
                }
            }
            grid.add(line);
        }
        scanner.close();

        return new Grid(grid);
    }

    private static int getNumTrees(String name, int right, int down) throws IOException {
        Grid grid = parse(name);

        int currentX = 0;
        int currentY = 0;
        int numTrees = 0;
        while (currentY < grid.getHeight()) {
            if (grid.get(currentX, currentY)) {
                numTrees++;
            }
            currentX += right;
            currentY += down;
        }
        return numTrees;
    }

    private static void test() throws IOException {
        assert getNumTrees("input_test.txt", 1, 1) == 2: "Failed test input";
        assert getNumTrees("input_test.txt", 3, 1) == 7: "Failed test input";
        assert getNumTrees("input_test.txt", 5, 1) == 3: "Failed test input";
        assert getNumTrees("input_test.txt", 7, 1) == 4: "Failed test input";
        assert getNumTrees("input_test.txt", 1, 2) == 2: "Failed test input";

        System.out.println("All tests passed");
    }

    private static long mult() throws IOException {
        long a = getNumTrees("input.txt", 1, 1);
        long b = getNumTrees("input.txt", 3, 1);
        long c = getNumTrees("input.txt", 5, 1);
        long d = getNumTrees("input.txt", 7, 1);
        long e = getNumTrees("input.txt", 1, 2);
        return (long) a * b * c * d * e;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(mult());
        // test();
    }
}