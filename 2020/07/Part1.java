import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Part1 {

    private static HashMap<String, HashSet<String>> parseInput(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        HashMap<String, HashSet<String>> bagContainedIn = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(" bags contain ");
            myAssert(splitLine.length == 2, "Line should contain ' bags contain '");
            String outer = splitLine[0];
            String inner = splitLine[1];
            if (!inner.equals("no other bags.")) {
                String[] splitInner = inner.split(", ");
                for (String innerBags: splitInner) {
                    String[] splitI = innerBags.split(" bag");
                    String[] splitInnerBags = splitI[0].split(" ", 2);
                    // int count = Integer.parseInt(splitInnerBags[0]);
                    String innerBag = splitInnerBags[1];
                    if (!bagContainedIn.containsKey(innerBag)) {
                        HashSet<String> containedIn = new HashSet<>();
                        bagContainedIn.put(innerBag, containedIn);
                    }
                    bagContainedIn.get(innerBag).add(outer);
                }
            }
        }
        scanner.close();

        return bagContainedIn;
    }

    private void recurse(String current) {
        this.colours.add(current);
        if (this.bagsContainedIn.containsKey(current)) {
            for (String bag: this.bagsContainedIn.get(current)) {
                recurse(bag);
            }
        }
    }

    HashMap<String, HashSet<String>> bagsContainedIn;
    HashSet<String> colours;
    public Part1(HashMap<String, HashSet<String>> bagsContainedIn) {
        this.bagsContainedIn = bagsContainedIn;
        this.colours = new HashSet<>();
    }

    private static int getNumBags(String name) throws IOException {
        Part1 instance = new Part1(parseInput(name));

        instance.recurse("shiny gold");

        return instance.colours.size() - 1; // subtract shiny gold
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(getNumBags("input_test.txt") == 4, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws IOException {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(getNumBags("input.txt"));
        }
    }
}