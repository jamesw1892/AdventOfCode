import java.io.IOException;
import java.util.ArrayList;

public class Part1 {

    private static int getAns(ArrayList<Instruction> instructions) {

        int x = 0;      // east is positive
        int y = 0;      // north is positive
        int dir = 90;   // clockwise from north - starts east

        for (Instruction instruction: instructions) {
            switch (instruction.getDir()) {
                case NORTH: y += instruction.getNum(); break;
                case SOUTH: y -= instruction.getNum(); break;
                case EAST: x += instruction.getNum(); break;
                case WEST: x -= instruction.getNum(); break;
                case LEFT: dir = (dir + 360 - instruction.getNum()) % 360; break;
                case RIGHT: dir = (dir + instruction.getNum()) % 360; break;
                case FORWARD:
                    switch (dir) {
                        case 0: y += instruction.getNum(); break;
                        case 90: x += instruction.getNum(); break;
                        case 180: y -= instruction.getNum(); break;
                        case 270: x -= instruction.getNum(); break;
                        default: Shared.myAssert(false, "Directions not divisible by 90 degrees!");
                    }
                    break;
                default: Shared.myAssert(false, "Invalid direction!");
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    private static int solve(String name) throws IOException {
        return getAns(Shared.parseInput(name));
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 25, "Failed test input");

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