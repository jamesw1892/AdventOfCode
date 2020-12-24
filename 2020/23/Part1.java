public class Part1 {
    private static String solve(String cups, int numMoves) {

        Circle circle = new Circle(cups);

        circle.move(numMoves);

        return circle.toString();
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() {

        myAssert(new Circle("389125467").toString().equals("25467389"), "Circle to string failed");
        myAssert(solve("389125467", 10).equals("92658374"), "Failed test input with 10 moves");
        myAssert(solve("389125467", 100).equals("67384529"), "Failed test input with 100 moves");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("598162734", 100));
        }
    }
}