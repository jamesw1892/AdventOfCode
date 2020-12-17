import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Part1 {

    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<Pos3D, Boolean> initial = new HashMap<>();

        int y = 0;
        int x = 0;
        while (scanner.hasNextLine()) {
            x = 0;
            for (char chr: scanner.nextLine().toCharArray()) {
                initial.put(new Pos3D(x, y, 0), chr == '#');
                x++;
            }
            y++;
        }
        scanner.close();

        GridInf3D grid = new GridInf3D(initial, 0, x-1, 0, y-1, 0, 0);

        grid.cycles(6);

        return grid.getNumActive();
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 112, "Failed test input");

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