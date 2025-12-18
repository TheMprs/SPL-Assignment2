import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    public static void testVectorCreation(){
        double[] arr ={1.0,2.0,3.0};
        SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR); 
        
        // Modify the original source array
        arr[0] = 999.0;
        
        // check vector init was successful 
        TestUtils.checkObject("Vector Creation", testVector,null);
    
        // If successful, the vector remains unchanged
        TestUtils.checkObject("Vector Deep Copy", testVector.get(0), 1.0);
    }
    
    public static void testVectorStatistics(){
        double[] arr ={1.0,2.0,3.0};
        SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
        
        //should print "1.0 2.0 3.0 "
        String str="";
        for (int i = 0; i < testVector.length(); i++) {
            str+=(testVector.get(i))+" ";
        }
        
        TestUtils.checkObject("Vector Get", str, "1.0 2.0 3.0 ");
        TestUtils.checkObject("Vector Length", testVector.length(), 3);
        TestUtils.checkObject("Vector Orientation", testVector.getOrientation(), VectorOrientation.ROW_MAJOR);
    }
    
    public static void testVectorAdd(){
        double[] arr1 ={1.0,2.0,3.0};
        double[] arr2 ={4.0,5.0,6.0};
        double[] arrsum1 = {5.0,7.0,9.0};
        double[] arrsum2 ={10.0,14.0,18.0};
        
        SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
        SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
        SharedVector sumVector1 = new SharedVector(arrsum1, VectorOrientation.ROW_MAJOR);
        SharedVector sumVector2 = new SharedVector(arrsum2, VectorOrientation.ROW_MAJOR);

        vector1.add(vector2); //perform simple addition        
        TestUtils.checkObject("Vector Simple Addition", vector1, sumVector1);
    
        vector1.add(vector1); //perform self addition
        TestUtils.checkObject("Vector Self Addition", vector1, sumVector2);
    }

    public static void testNegate(){
        double[] arr1 ={1.0,2.0,3.0};
        double[] arr1neg ={-1.0,-2.0,-3.0};
        double[] arr2 ={0.0,0.0};
        double[] arr2neg ={-0.0,-0.0};
        
        
        SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
        SharedVector vector1neg = new SharedVector(arr1neg, VectorOrientation.ROW_MAJOR);
        SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
        SharedVector vector2neg = new SharedVector(arr2neg, VectorOrientation.ROW_MAJOR);
        SharedVector nullVector=null;
        
        vector1.negate();
        vector2.negate();
        
        TestUtils.checkObject("Negate Check", vector1, vector1neg);
        TestUtils.checkObject("Negate Zero Check", vector2, vector2neg);
        // try{
        //     nullVector.negate();
        // }
        // catch(NullPointerException){
            
        // }
    }

}
