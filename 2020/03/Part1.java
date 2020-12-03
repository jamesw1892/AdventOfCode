import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {

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

    private static int getNumTrees(String name) throws IOException {
        Grid grid = parse(name);

        int currentX = 0;
        int currentY = 0;
        int right = 3;
        int down = 1;
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
        assert getNumTrees("input_test.txt") == 7: "Failed test input";

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumTrees("input.txt"));
        }
    }
}