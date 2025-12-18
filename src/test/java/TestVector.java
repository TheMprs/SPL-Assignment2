import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    public void testVectorCreation(){
        double[] arr ={1.0,2.0,3.0};
        SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR); 
        
        // Modify the original source array
        arr[0] = 999.0;
        
        // check vector init was successful 
        TestUtils.check("Vector Creation", testVector!=null);
    
        // If successful, the vector remains unchanged
        TestUtils.check("Vector Deep Copy", testVector.get(0) == 1.0);
        }
    
        public void testVectorStatistics(){
        double[] arr ={1.0,2.0,3.0};
        SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
        
        //should print "1.0 2.0 3.0 "
        String str="";
        for (int i = 0; i < testVector.length(); i++) {
            str+=(testVector.get(i))+" ";
        }
        
        TestUtils.check("Vector Get", str == "1.0 2.0 3.0 ");
        TestUtils.check("Vector Length", testVector.length() == 3);
        TestUtils.check("Vector Orientation", testVector.getOrientation() == VectorOrientation.ROW_MAJOR);
    }
    public void testVectorAdd(){
        double[] arr1 ={1.0,2.0,3.0};
        SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
        
        double[] arr2 ={4.0,5.0,6.0};
        SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
        
        double[] arr3 ={-1.0,-2.0,-3.0};
        SharedVector vector3 = new SharedVector(arr3, VectorOrientation.ROW_MAJOR);
        

    }

}
