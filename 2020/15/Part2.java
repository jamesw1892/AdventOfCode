public class Part2 {

    private static void test() {

        Shared.myAssert(Shared.solve("0,3,6", 30000000) == 175594L, "Failed test input 0");
        Shared.myAssert(Shared.solve("1,3,2", 30000000) == 2578L, "Failed test input 1");
        Shared.myAssert(Shared.solve("2,1,3", 30000000) == 3544142L, "Failed test input 2");
        Shared.myAssert(Shared.solve("1,2,3", 30000000) == 261214L, "Failed test input 3");
        Shared.myAssert(Shared.solve("2,3,1", 30000000) == 6895259L, "Failed test input 4");
        Shared.myAssert(Shared.solve("3,2,1", 30000000) == 18L, "Failed test input 5");
        Shared.myAssert(Shared.solve("3,1,2", 30000000) == 362L, "Failed test input 6");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(Shared.solve("10,16,6,0,1,17", 30000000));
        }
    }
}