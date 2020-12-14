import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Part2 {

    private static long sum(HashMap<Long, Long> memory) {
        long total = 0L;
        for (Long key: memory.keySet()) {
            total += memory.get(key);
        }
        return total;
    }

    private static void setValue(HashMap<Long, Long> memory, long memAdd, long value, String mask) {

        String memAddS = Long.toBinaryString(memAdd);
        while (memAddS.length() < mask.length()) {
            memAddS = '0' + memAddS;
        }
        String memAddress = "";
        for (int i = 0; i < mask.length(); i++) {
            char chr = mask.charAt(i);
            switch (chr) {
                case 'X':
                case '1':
                    memAddress += chr;
                    break;
                case '0':
                    memAddress += memAddS.charAt(i);
                    break;
                default:
                    myAssert(false, "Invalid mask character");
            }
        }

        ArrayList<String> newMemAddresses = new ArrayList<>();
        newMemAddresses.add("");
        for (int i = 0; i < memAddress.length(); i++) {
            char at = memAddress.charAt(i);
            ArrayList<String> temp = new ArrayList<>();
            if (at == 'X') {
                for (String s: newMemAddresses) {
                    temp.add(s + '0');
                    temp.add(s + '1');
                }
            } else {
                for (String s: newMemAddresses) {
                    temp.add(s + at);
                }
            }
            newMemAddresses = temp;
        }

        for (String memoryAddress: newMemAddresses) {
            memory.put(Long.parseLong(memoryAddress, 2), value);
        }
    }

    private static long solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<Long, Long> nonZeroMemory = new HashMap<>();
        String mask = "";
        long memAdd;
        long value;
        String[] splitLine;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("mask")) {
                mask = line.split("mask = ")[1];
            } else {
                splitLine = line.split("mem\\[")[1].split("\\] = ");
                memAdd = Long.parseLong(splitLine[0]);
                value = Long.parseLong(splitLine[1]);
                setValue(nonZeroMemory, memAdd, value, mask);
            }
        }
        scanner.close();

        return sum(nonZeroMemory);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test_part_2.txt") == 208L, "Failed test input");

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