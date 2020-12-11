import java.io.IOException;

public class Part2 {

    private static int solve(String name) throws IOException {

        GridPart2 grid = new GridPart2(Shared.parseInput(name));

        while (grid.applyRules());

        return grid.getNumOccupied();
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 26, "Failed test input");

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