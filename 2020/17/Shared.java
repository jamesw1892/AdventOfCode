public class Shared {
    public static void myAssert(boolean condition, String msg) {
        if (!condition) throw new AssertionError(msg);
    }
}