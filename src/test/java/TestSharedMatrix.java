

import java.util.Vector;

import memory.SharedMatrix;
import memory.SharedVector;
import memory.VectorOrientation;

public class TestSharedMatrix {
    public static void main(String[] args) {
        System.out.println(" --- TESTING SHARED MATRIX FUNCTIONS ---");
        
        System.out.println("\n-- Testing Matrix Row Major Load --");
        testMatrixRowMajorLoad();

        System.out.println("\n-- Testing Matrix Column Major Load --");
        testMatrixColumnMajorLoad();
        
        System.out.println("\n-- Testing Matrix Row Major Read --");
        testMatrixRowMajorRead();
        
        System.out.println("\n-- Testing Matrix Stats --");
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
    

        System.out.println("First row of Row Major Matrix:");
        System.out.println(matrix.get(0));    
    }
    
    public static void testMatrixColumnMajorLoad() {
        double[][] table = { {1,2}, {3,4} };
        SharedMatrix matrix = new SharedMatrix(); 
        matrix.loadColumnMajor(table);

        double[] arr = {1.0,3.0};
        SharedVector firstCol = new SharedVector(arr,VectorOrientation.COLUMN_MAJOR);

        TestUtils.checkObject("Matrix Column Major Orientation", matrix.getOrientation(), VectorOrientation.COLUMN_MAJOR);
        TestUtils.checkObject("Matrix Column Major Content", matrix.get(0),firstCol);
        
        System.out.println("First row of Column Major Matrix:");
        System.out.println(matrix.get(0));
    }
    
    public static void testMatrixRowMajorRead() {
        double[][] table = { {1,2}, {3,4} };
        double[][] line = { {1,2,3} };

        SharedMatrix rowTable = new SharedMatrix(); 
        SharedMatrix colTable = new SharedMatrix();
        SharedMatrix rowLine = new SharedMatrix(); 
        SharedMatrix colLine = new SharedMatrix();

        rowTable.loadRowMajor(table);
        colTable.loadColumnMajor(table);
        rowLine.loadRowMajor(line);
        colLine.loadColumnMajor(line);

        double[][] readRowTable = rowTable.readRowMajor();
        double[][] readColTable = colTable.readRowMajor();
        double[][] readRowLine = rowLine.readRowMajor();
        double[][] readColLine = colLine.readRowMajor();    

        System.out.println("Row Major Matrix:");
        System.out.print(rowTable);
        System.out.println("Read Row Major Table:");
        for (double[] row : readRowTable) {
            System.out.print("[ ");
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println("]");
        }

        System.out.println("Column Major Matrix:");
        System.out.print(colTable);
        System.out.println("Read Column Major Table:");
        for (double[] row : readColTable) {
            System.out.print("[ ");
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println("]");
        }

        System.out.println("Row Major Line:");
        System.out.print(rowLine);
        System.out.println("Read Row Major Line:");
        for (double[] row : readRowLine) {
            System.out.print("[ ");
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println("]");
        }

        System.out.println("Column Major Line:");
        System.out.print(colLine);
        System.out.println("Read Column Major Line:");
        for (double[] row : readColLine) {
            System.out.print("[ ");
            for (double val : row) {
                System.out.print(val + " ");
            }
            System.out.println("]");
        }
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

