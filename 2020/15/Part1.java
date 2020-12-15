public class Part1 {

    private static void test() {

        Shared.myAssert(Shared.solve("0,3,6", 2020) == 436L, "Failed test input 0");
        Shared.myAssert(Shared.solve("1,3,2", 2020) == 1L, "Failed test input 1");
        Shared.myAssert(Shared.solve("2,1,3", 2020) == 10L, "Failed test input 2");
        Shared.myAssert(Shared.solve("1,2,3", 2020) == 27L, "Failed test input 3");
        Shared.myAssert(Shared.solve("2,3,1", 2020) == 78L, "Failed test input 4");
        Shared.myAssert(Shared.solve("3,2,1", 2020) == 438L, "Failed test input 5");
        Shared.myAssert(Shared.solve("3,1,2", 2020) == 1836L, "Failed test input 6");

        System.out.println("All tests passed");
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("test")) {
            test();
        } else {
            System.out.println(Shared.solve("10,16,6,0,1,17", 2020));
        }
    }
}