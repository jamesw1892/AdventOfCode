import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Part1 {
    private static int play(ArrayDeque<Integer> p1, ArrayDeque<Integer> p2) {

        // play
        while (!p1.isEmpty() && !p2.isEmpty()) {
            Integer c1 = p1.remove();
            Integer c2 = p2.remove();
            if (c1 > c2) {
                p1.add(c1);
                p1.add(c2);
            } else {
                p2.add(c2);
                p2.add(c1);
            }
        }

        // calc score
        ArrayDeque<Integer> winner;
        if (p1.isEmpty()) {
            winner = p2;
        } else {
            winner = p1;
        }

        int points = winner.size();
        int score = 0;
        while (!winner.isEmpty()) {
            score += points * winner.remove();
            points--;
        }

        return score;
    }

    private static int solve(String name) throws IOException {
        FileInputStream file = new FileInputStream(name);
        Scanner scanner = new Scanner(file);

        ArrayDeque<Integer> p1 = new ArrayDeque<>();
        ArrayDeque<Integer> p2 = new ArrayDeque<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                break;
            } else if (!line.equals("Player 1:")) {
                p1.add(Integer.parseInt(line));
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.equals("Player 2:")) {
                p2.add(Integer.parseInt(line));
            }
        }
        scanner.close();

        return play(p1, p2);
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() throws IOException {

        myAssert(solve("input_test.txt") == 306, "Failed test input");

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