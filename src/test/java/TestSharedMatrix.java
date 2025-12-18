import java.util.Vector;

import memory.SharedMatrix;
import memory.SharedVector;
import memory.VectorOrientation;

public class TestSharedMatrix {
    public static void main(String[] args) {
        System.out.println(" --- TESTING SHARED MATRIX FUNCTIONS ---");
        testMatrixRowMajorLoad();
        testMatrixColumnMajorLoad();
        testMatrixStats();

        TestUtils.summary();
        
        System.out.println(" --- END OF SHARED MATRIX TESTING ---");
    }

    public static void testMatrixRowMajorLoad() {
        double[][] table = { {1,2}, {3,4} };
        double[][] tinyTable = {{4.0}};

        SharedMatrix matrix = new SharedMatrix(); 
        SharedMatrix tinyMatrix = new SharedMatrix();
        
        matrix.loadRowMajor(table);
        tinyMatrix.loadRowMajor(tinyTable);

        TestUtils.checkObject("Matrix Row Major Orientation", matrix.getOrientation(), VectorOrientation.ROW_MAJOR);
        TestUtils.checkObject("Matrix Row Major Content", matrix.get(0).get(1), 2.0);
    
        TestUtils.checkObject("Tiny Matrix Row Major Orientation", tinyMatrix.getOrientation(), VectorOrientation.ROW_MAJOR);
        TestUtils.checkObject("Matrix Row Major Content", tinyMatrix.get(0).get(0), 4.0);
    

    }
    
    public static void testMatrixColumnMajorLoad() {
        double[][] table = { {1,2}, {3,4} };
        SharedMatrix matrix = new SharedMatrix(); 
        matrix.loadColumnMajor(table);

        double[] arr = {1.0,3.0};
        SharedVector firstCol = new SharedVector(arr,VectorOrientation.COLUMN_MAJOR);

        TestUtils.checkObject("Matrix Column Major Orientation", matrix.getOrientation(), VectorOrientation.COLUMN_MAJOR);
        TestUtils.checkObject("Matrix Column Major Content", matrix.get(0),firstCol);
    }
    
    public static void testMatrixStats(){
        double[][] table = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
        };
        
        SharedMatrix emptyMatrix = new SharedMatrix();
        SharedMatrix matrix = new SharedMatrix(table);
        
        TestUtils.checkObject("Matrix get", matrix.get(0).get(0), 1.0);
        TestUtils.checkObject("Matrix Column length", matrix.length(), 2);
        TestUtils.checkObject("Matrix Row length", matrix.get(0).length(), 3);
        TestUtils.checkObject("Matrix Orientation", matrix.getOrientation(), VectorOrientation.ROW_MAJOR);
        
        try {
            emptyMatrix.get(0);
            System.out.println("[FAIL] Did not throw exception: Index out of bounds");
        } 
        catch (Exception e) {
            System.out.println("[PASS] Get Index Error Caught");
        }
        TestUtils.checkObject("Empty Matrix length", emptyMatrix.length(), 0 );
        try {
            emptyMatrix.getOrientation();
            System.out.println("[FAIL] Index out of bounds");
        } catch (Exception e) {
            System.out.println("[PASS] Matrix Orientation Undefined");
        }
    }
}

