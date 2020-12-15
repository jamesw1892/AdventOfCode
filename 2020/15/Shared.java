import java.util.HashMap;

public class Shared {
    public static long solve(String input, int numToFind) {

        HashMap<Long, Integer> prevTurnSaid = new HashMap<>();

        int turnNum = 1;
        long prev = 0L;
        boolean first = true;

        for (String s: input.split(",")) {
            if (first) {
                first = false;
            } else {
                prevTurnSaid.put(prev, turnNum-1);
            }
            prev = Long.parseLong(s);
            turnNum++;
        }

        Integer turnSaid;

        while (turnNum <= numToFind) {

            turnSaid = prevTurnSaid.get(prev);
            prevTurnSaid.put(prev, turnNum-1);
            if (turnSaid == null) {
                prev = 0L;
            } else {
                prev = turnNum - 1 - turnSaid;
            }

            turnNum++;
        }

        return prev;
    }

    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }
}