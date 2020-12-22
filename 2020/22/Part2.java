import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Scanner;

class Pair<P, Q> {
    P p;
    Q q;
    public Pair(P p, Q q) {
        this.p = p;
        this.q = q;
    }
}

public class Part2 {

    /**
     * Copy the specified number into a new deque without editing hand
     */
    private static ArrayDeque<Integer> copy(ArrayDeque<Integer> hand, int numCards) {
        ArrayDeque<Integer> n = new ArrayDeque<>();
        for (Integer num: hand) {
            if (numCards > 0) {
                n.add(num);
            } else {
                break;
            }
            numCards--;
        }
        return n;
    }

    /**
     * Write custom equals and contains methods as built-in ones weren't working correctly
     */
    private static boolean myEquals(ArrayDeque<Integer> item, ArrayDeque<Integer> config) {
        if (item.size() != config.size()) {
            return false;
        }
        Integer[] itemArray = item.toArray(new Integer[0]);
        Integer[] configArray = config.toArray(new Integer[0]);
        for (int index = 0; index < itemArray.length; index++) {
            if (itemArray[index] != configArray[index]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Write custom equals and contains methods as built-in ones weren't working correctly
     */
    private static boolean myEquals(Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> item, Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> configuration) {
        return myEquals(item.p, configuration.p) && myEquals(item.q, configuration.q);
    }

    /**
     * Write custom equals and contains methods as built-in ones weren't working correctly
     */
    private static boolean myContains(HashSet<Pair<ArrayDeque<Integer>, ArrayDeque<Integer>>> handsSeen, Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> configuration) {
        for (Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> item: handsSeen) {
            if (myEquals(item, configuration)) {
                return true;
            }
        }
        return false;
    }

    /**
     * True means player 1 won, false means player 2 won
     */
    private static boolean subGame(ArrayDeque<Integer> p1, ArrayDeque<Integer> p2) {

        HashSet<Pair<ArrayDeque<Integer>, ArrayDeque<Integer>>> handsSeen = new HashSet<>();

        while (!p1.isEmpty() && !p2.isEmpty()) {

            Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> configuration = new Pair<>(p1.clone(), p2.clone());

            // if already seen, player 1 wins the game instantly
            if (myContains(handsSeen, configuration)) {
                return true;
            }
            handsSeen.add(configuration);

            Integer c1 = p1.remove();
            Integer c2 = p2.remove();

            boolean p1WinsRound;
            if (p1.size() >= c1 && p2.size() >= c2) {
                if (subGame(copy(p1, c1), copy(p2, c2))) {
                    p1WinsRound = true;
                } else {
                    p1WinsRound = false;
                }
            } else {
                if (c1 > c2) {
                    p1WinsRound = true;
                } else {
                    p1WinsRound = false;
                }
            }

            if (p1WinsRound) {
                p1.add(c1);
                p1.add(c2);
            } else {
                p2.add(c2);
                p2.add(c1);
            }
        }

        // if p2 is empty, p1 wins (return true), otherwise p1 is empty so p2 wins (return false)
        return p2.isEmpty();
    }

    private static int play(ArrayDeque<Integer> p1, ArrayDeque<Integer> p2) {

        HashSet<Pair<ArrayDeque<Integer>, ArrayDeque<Integer>>> handsSeen = new HashSet<>();

        boolean p1WinsGame = false;

        while (!p1.isEmpty() && !p2.isEmpty()) {

            Pair<ArrayDeque<Integer>, ArrayDeque<Integer>> configuration = new Pair<>(p1.clone(), p2.clone());

            // if already seen, player 1 wins the game instantly
            if (myContains(handsSeen, configuration)) {
                p1WinsGame = true;
                break;
            }
            handsSeen.add(configuration);

            Integer c1 = p1.remove();
            Integer c2 = p2.remove();

            boolean p1WinsRound;
            if (p1.size() >= c1 && p2.size() >= c2) {
                if (subGame(copy(p1, c1), copy(p2, c2))) {
                    p1WinsRound = true;
                } else {
                    p1WinsRound = false;
                }
            } else {
                if (c1 > c2) {
                    p1WinsRound = true;
                } else {
                    p1WinsRound = false;
                }
            }

            if (p1WinsRound) {
                p1.add(c1);
                p1.add(c2);
            } else {
                p2.add(c2);
                p2.add(c1);
            }
        }

        // calc score of winner
        ArrayDeque<Integer> winner;
        if (p1WinsGame || p2.isEmpty()) {
            winner = p1;
        } else {
            winner = p2;
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

        myAssert(solve("input_test.txt") == 291, "Failed test input");

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