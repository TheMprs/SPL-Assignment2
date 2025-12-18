public class TestUtils {
    private static void checkObject(String testName, Object actual, Object expected) {
        if (actual.equals(expected)) {
            System.out.println("[PASS] " + testName);
        } 
        else {
            System.out.println("[FAIL] " + testName);
            System.out.println("   -> Expected: " + expected.toString());
            System.out.println("   -> Actual:   " + actual.toString());
        }
    }
}
