import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Part2 {

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

    private static Integer accumulatorWhenTerminates(String name) throws IOException {
        ArrayList<String> originalInstructions = parseInput(name);
        ArrayList<ArrayList<String>> possibleInstructions = new ArrayList<>();

        for (int i = 0; i < originalInstructions.size(); i++) {
            String instructionStr = originalInstructions.get(i);
            Instruction instruction = parseInstruction(instructionStr);
            if (instruction.operation.equals("jmp") || instruction.operation.equals("nop")) {
                @SuppressWarnings("unchecked")
                ArrayList<String> newInstructions = (ArrayList<String>) originalInstructions.clone();
                String newInstruction = instructionStr;
                if (instruction.operation.equals("jmp")) {
                    newInstruction = newInstruction.replace("jmp", "nop");
                } else {
                    newInstruction = newInstruction.replace("nop", "jmp");
                }
                newInstructions.set(i, newInstruction);
                possibleInstructions.add(newInstructions);
            }
        }

        for (ArrayList<String> instructions: possibleInstructions) {
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

            // this instruction set works, otherwise keep looping
            if (currentInstructionNum == instructions.size()) {
                return accumulator;
            } else if (currentInstructionNum > instructions.size()) {
                System.out.println("Exceeded length");
            }
        }

        return null;
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(accumulatorWhenTerminates("input_test.txt") == 8, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(accumulatorWhenTerminates("input.txt"));
        }
    }
}