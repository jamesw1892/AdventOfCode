import java.io.IOException;
import java.util.ArrayList;

public class Part2 {

    private static int getAns(ArrayList<Instruction> instructions) {

        // relative to ship
        int wayX = 10;
        int wayY = 1;

        int shipX = 0;  // east is positive
        int shipY = 0;  // north is positive
        int temp;

        for (Instruction instruction: instructions) {
            switch (instruction.getDir()) {
                case NORTH: wayY += instruction.getNum(); break;
                case SOUTH: wayY -= instruction.getNum(); break;
                case EAST: wayX += instruction.getNum(); break;
                case WEST: wayX -= instruction.getNum(); break;
                case LEFT:
                    for (int i = 0; i < instruction.getNum() / 90; i++) {
                        // rotate anti-clockwise by 90 degrees about origin (as relative to ship)
                        // so x becomes -y and y becomes x
                        temp = wayX;
                        wayX = - wayY;
                        wayY = temp;
                    }
                    break;
                case RIGHT:
                    for (int i = 0; i < instruction.getNum() / 90; i++) {
                        // rotate clockwise by 90 degrees about origin (as relative to ship)
                        // so x becomes y and y becomes -x
                        temp = wayX;
                        wayX = wayY;
                        wayY = - temp;
                    }
                    break;
                case FORWARD:
                    shipX += instruction.getNum() * wayX;
                    shipY += instruction.getNum() * wayY;
                    break;
                default: Shared.myAssert(false, "Invalid direction!");
            }
        }
        return Math.abs(shipX) + Math.abs(shipY);
    }

    private static int solve(String name) throws IOException {
        return getAns(Shared.parseInput(name));
    }

    private static void test() throws IOException {

        Shared.myAssert(solve("input_test.txt") == 286, "Failed test input");

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