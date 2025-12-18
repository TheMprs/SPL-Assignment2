import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    public void testVCreation(){
        //test vector creation
        double[] arrtest1 = {1,2,3};
        SharedVector vectortest1 = new SharedVector(arrtest1, VectorOrientation.ROW_MAJOR); 

        System.out.println("Test simple vector print:");
        
        for (int i = 0; i < vectortest1.length(); i++) {
            System.out.print(vectortest1.get(i));
        }
    }

}
