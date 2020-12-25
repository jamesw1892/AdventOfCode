import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Part1 {

    private static long transformSubjectNum(long subjectNum, long loopSize) {
        long value = 1;
        for (long i = 0; i < loopSize; i++) {
            value = (value * subjectNum) % 20201227L;
        }
        return value;
    }

    private static long bruteForceLoopSize(long subjectNum, long value) {
        long newValue = 1;
        long loopSize = 0;

        do {
            newValue *= subjectNum;
            newValue %= 20201227L;
            loopSize++;
        } while (newValue != value);

        return loopSize;
    }

    private static long solve(String filename) throws IOException {
        FileInputStream file = new FileInputStream(filename);
        Scanner scanner = new Scanner(file);

        long cardPubKey = scanner.nextLong();
        long doorPubKey = scanner.nextLong();
        scanner.close();

        long cardLoopSize = bruteForceLoopSize(7, cardPubKey);
        long doorLoopSize = bruteForceLoopSize(7, doorPubKey);

        myAssert(transformSubjectNum(7, cardLoopSize) == cardPubKey, "Card pub key check");
        myAssert(transformSubjectNum(7, doorLoopSize) == doorPubKey, "Door pub key check");

        long keyCard = transformSubjectNum(doorPubKey, cardLoopSize);
        long keyDoor = transformSubjectNum(cardPubKey, doorLoopSize);

        myAssert(keyCard == keyDoor, "Keys must be equal");

        return keyCard;
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 14897079L, "Failed test input");

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