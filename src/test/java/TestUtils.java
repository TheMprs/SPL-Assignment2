public class TestUtils {
    public static void check (String name, Boolean condition){
        if (condition){
            System.out.println(name+": PASS");
        }
        else{
            System.out.println(name+ ": FAIL");
        }
    }
}
