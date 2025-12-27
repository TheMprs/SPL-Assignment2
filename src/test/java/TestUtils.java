

public class TestUtils {
    
    public static void checkObject(String testName, Object actual, Object expected) {
        if (actual.equals(expected)) {
            System.out.println("[PASS] " + testName);
        } 
        else {
            throw new AssertionError("[FAIL] " + testName + "\n" +
             "   -> Expected: " + expected.toString() + "\n" +
             "   -> Actual:   " + actual.toString());
        }
    }
}
