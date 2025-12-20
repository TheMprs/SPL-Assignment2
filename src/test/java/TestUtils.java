

public class TestUtils {
    
    public static int err=0;
    
    public static void checkObject(String testName, Object actual, Object expected) {
        if (actual.equals(expected)) {
            System.out.println("[PASS] " + testName);
        } 
        else {
            System.out.println("[FAIL] " + testName);
            System.out.println("   -> Expected: " + expected.toString());
            System.out.println("   -> Actual:   " + actual.toString());
            err+=1;
        }
    }
    public static void summary(){
        System.out.println("TESTS CONCLUDED: "+err+" errors found!");
    }
}
