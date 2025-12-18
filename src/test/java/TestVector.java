import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    double[] arrtest1 = {1,2,3};
    SharedVector vectortest1 = new SharedVector(arrtest1, VectorOrientation.ROW_MAJOR); 

    
    for (int i = 0; i < vectortest1.length(); i++) {
        System.out.print(vectortest1.get(i));
    }
}
