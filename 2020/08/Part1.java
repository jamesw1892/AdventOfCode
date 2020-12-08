import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Part1 {

    private static ArrayList<String> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayList<String> instructions = new ArrayList<>();

        while (scanner.hasNextLine()) {
            instructions.add(scanner.nextLine());
        }
        scanner.close();

        return instructions;
    }

    private static Instruction parseInstruction(String instruction) {
        String[] splitInstruction = instruction.split(" ");
        myAssert(splitInstruction.length == 2, "Every instruction should contain exactly 1 space");
        return new Instruction(splitInstruction[0], Integer.parseInt(splitInstruction[1]));
    }

    private static int accumulatorBeforeLoop(String name) throws IOException {
        ArrayList<String> instructions = parseInput(name);
        HashSet<Integer> instructionNumsAlreadyExecuted = new HashSet<>();

        int accumulator = 0;
        int currentInstructionNum = 0;

        while (!instructionNumsAlreadyExecuted.contains(currentInstructionNum) && currentInstructionNum < instructions.size()) {
            instructionNumsAlreadyExecuted.add(currentInstructionNum);
            Instruction instruction = parseInstruction(instructions.get(currentInstructionNum));
            switch (instruction.operation) {
                case "acc":
                    accumulator += instruction.argument;
                case "nop":
                    currentInstructionNum++;
                    break;
                case "jmp":
                    currentInstructionNum += instruction.argument;
                    break;
                default:
                    myAssert(false, "Valid operations are only 'acc', 'nop' and 'jmp'");
            }
        }

        return accumulator;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(accumulatorBeforeLoop("input_test.txt") == 5, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(accumulatorBeforeLoop("input.txt"));
        }
    }
}