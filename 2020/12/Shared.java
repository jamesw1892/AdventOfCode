import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Shared {
    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    public static ArrayList<Instruction> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<Instruction> instructions = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char chr = line.charAt(0);
            StringBuilder s = new StringBuilder(line);
            s.deleteCharAt(0);
            int num = Integer.parseInt(s.toString());
            instructions.add(new Instruction(Direction.fromChar(chr), num));
        }
        scanner.close();
        return instructions;
    }
}