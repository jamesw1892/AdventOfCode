public class Part2 {
    private static long solve(String cups, int numMoves) {

        Circle circle = new Circle(cups, 1000000);

        circle.move(numMoves);

        return circle.product();
    }

    private static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }

    private static void test() {

        myAssert(solve("389125467", 10000000) == 149245887792L, "Failed test input");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(solve("598162734", 10000000));
        }
    }
}