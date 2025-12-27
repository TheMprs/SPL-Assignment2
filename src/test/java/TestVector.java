import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import memory.SharedMatrix;
import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    @Test public void testVectorCreation(){
        System.out.println(" \n-- Testing Vector Creation --");
        //test valid vector creation row-major
        {

            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            //check orientation match
            if(testVector.getOrientation() != VectorOrientation.ROW_MAJOR) {
                System.out.println("[FAIL] Row-Major Vector Creation Orientation Check");
            } else {
                System.out.println("[PASS] Row-Major Vector Creation Orientation Check");
            }
            
            //check length match
            if(testVector.length() != 3) {
                System.out.println("[FAIL] Row-Major Vector Creation Length Check");
            } else {
                System.out.println("[PASS] Row-Major Vector Creation Length Check");
            }

            //check values match
            boolean valuesCorrect = true;
            for (int i = 0; i < testVector.length() && valuesCorrect; i++) {
                if(testVector.get(i) != arr[i]) {
                    System.out.println("[FAIL] Row-Major Vector Values Check at index "+i+": expected "+arr[i]+", got "+testVector.get(i));
                    valuesCorrect = false;
                }
            }
            if(valuesCorrect) { System.out.println("[PASS] Row-Major Vector Values Check"); }
        }
        //test valid vector creation column-major
        {
            double[] arr ={4.0,5.0,6.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.COLUMN_MAJOR);
            
            //check orientation match
            if(testVector.getOrientation() != VectorOrientation.COLUMN_MAJOR) {
                System.out.println("[FAIL] Column-Major Vector Creation Orientation Check");
            } else {
                System.out.println("[PASS] Column-Major Vector Creation Orientation Check");
            }
            
            //check length match
            if(testVector.length() != 3) {
                System.out.println("[FAIL] Column-Major Vector Creation Length Check");
            } else {
                System.out.println("[PASS] Column-Major Vector Creation Length Check");
            }

            //check values match
            boolean valuesCorrect = true;
            for (int i = 0; i < testVector.length() && valuesCorrect; i++) {
                if(testVector.get(i) != arr[i]) {
                    System.out.println("[FAIL] Column-Major Vector Values Check at index "+i+": expected "+arr[i]+", got "+testVector.get(i));
                    valuesCorrect = false;
                }
            }
            if(valuesCorrect) { System.out.println("[PASS] Column-Major Vector Values Check"); }
        }
        //test null orientation
        {
            double[] arr ={1.0,2.0,3.0};
            try{
                SharedVector brokenVector = new SharedVector(arr, null);
                System.out.println("[FAIL] Null Orientation: No exception thrown");
            }
            catch (IllegalArgumentException e){
                System.out.println("[PASS] Null Orientation: Caught exception");
            }
        }
        //test empty vector creation
        {
            double[] emptyArr = new double[0];
            try{
                SharedVector brokenVector = new SharedVector(emptyArr, VectorOrientation.ROW_MAJOR);
                System.out.println("[FAIL] Empty Vector Creation: No exception thrown");
            }
            catch (IllegalArgumentException e){
                System.out.println("[PASS] Empty Vector Creation: Caught exception");
            }

            double[] emptyArr2 = {};
            try{
                SharedVector brokenVector2 = new SharedVector(emptyArr2, VectorOrientation.COLUMN_MAJOR);
                System.out.println("[FAIL] Empty Vector Creation 2: No exception thrown");
            }
            catch (IllegalArgumentException e){
                System.out.println("[PASS] Empty Vector Creation 2: Caught exception");
            }
        }
        //test null vector creation
        {
            try{    
                SharedVector nullVector = new SharedVector(null, VectorOrientation.ROW_MAJOR);
                System.out.println("[FAIL] Null Vector Creation: No exception thrown");
            }
            catch (IllegalArgumentException e){
                System.out.println("[PASS] Null Vector Creation: Caught exception");
            }
        }
        //test deep copy
        {
            double[] deepCopyArr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(deepCopyArr, VectorOrientation.ROW_MAJOR); 
            
            // Modify the original source array
            deepCopyArr[0] = 999.0;
            
            // check vector init was successful 
            TestUtils.checkObject("Vector Creation", testVector.get(0),1.0);
        
            // If successful, the vector remains unchanged
            TestUtils.checkObject("Vector Deep Copy", testVector.get(0), 1.0);
        }
    }
    
    @Test public void testGetFunction(){
        System.out.println("\n-- Testing Vector Get Function --");
        //test get null vector
        {
            SharedVector nullVector = null;
            try{
                nullVector.get(0);
                System.out.println("[FAIL] Get on Null Vector: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Get on Null Vector: Caught exception");
            }
        }
        //test get negative index
        {
            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            try{
                testVector.get(-1);
                System.out.println("[FAIL] Get on Negative Index: No exception thrown");
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("[PASS] Get on Negative Index: Caught exception");
            }
        }
        //test get out-of-bounds index
        {
            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            try{
                testVector.get(3);
                System.out.println("[FAIL] Get on Out-of-Bounds Index: No exception thrown");
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("[PASS] Get on Out-of-Bounds Index: Caught exception");
            }
        }
        //test get during readlock
        {
            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            testVector.readLock();
            try{
                double val = testVector.get(1);
                TestUtils.checkObject("Get during ReadLock", val, 2.0);
            }
            catch (Exception e){
                System.out.println("[FAIL] Get during ReadLock: Exception thrown");
            }
            finally{
                testVector.readUnlock();
            }
        }
        //test get during writelock
        {
            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            testVector.writeLock();
            try{
                double val = testVector.get(1);
                TestUtils.checkObject("Get during WriteLock", val, 2.0);
            }
            catch (Exception e){
                System.out.println("[FAIL] Get during WriteLock: Exception thrown");
            }
            finally{
                testVector.writeUnlock();
            }
        }
        //test valid get operation
        {
            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            //should print "1.0 2.0 3.0 "
            String str="";
            for (int i = 0; i < testVector.length(); i++) {
                str+=(testVector.get(i))+" ";
            }
            
            TestUtils.checkObject("Vector Get", str, "1.0 2.0 3.0 ");
        }
    }
    
    @Test public void testVectorLength(){
        System.out.println("\n-- Testing Vector Length --");

        //test length of valid vector
        {
            double[] arr1 ={1.0,2.0,3.0,4.0,5.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            int length = vector1.length();
            
            TestUtils.checkObject("Valid Vector Length", length, 5);
        }
        //test length of null vector
        {
            SharedVector nullVector = null;
            
            try{
                int length = nullVector.length();
                System.out.println("[FAIL] Length of Null Vector: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Length of Null Vector: Caught exception");
            }
        }
        //test length during readlock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            try{
                int length = vector1.length();
                TestUtils.checkObject("Length during ReadLock", length, 3);
            }
            catch (Exception e){
                System.out.println("[FAIL] Length during ReadLock: Exception thrown");
            }
            finally{
                vector1.readUnlock();
            }
        }
        //test length during writelock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            try{
                int length = vector1.length();
                TestUtils.checkObject("Length during WriteLock", length, 3);
            }
            catch (Exception e){
                System.out.println("[FAIL] Length during WriteLock: Exception thrown");
            }
            finally{
                vector1.writeUnlock();
            }
        }
    }

    @Test public void testVectorAdd(){
        System.out.println("\n-- Testing Vector Addition --");
        //test simple addition
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            double[] arrsum1 = {5.0,7.0,9.0};
            SharedVector sumVector1 = new SharedVector(arrsum1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); //perform simple addition        
            TestUtils.checkObject("Vector Simple Addition", vector1, sumVector1);
        }
        //test self addition
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arrsum2 = {2.0,4.0,6.0};
            SharedVector sumVector2 = new SharedVector(arrsum2, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector1); //perform self addition        
            TestUtils.checkObject("Vector Self Addition", vector1, sumVector2);
        }
        //test addition with negative numbers
        {
            double[] arr1 ={1.0,-2.0,3.0};
            double[] arr2 ={-4.0,5.0,-6.0};
            double[] arrsum3 = {-3.0,3.0,-3.0};
            SharedVector sumVector3 = new SharedVector(arrsum3, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); //perform addition with negatives        
            TestUtils.checkObject("Vector Addition with Negatives", vector1, sumVector3);
        }
        //test addition with dimension mismatch
        {
            double[] arr3 = {1};
            double[] arr4 = {1,2};
            
            SharedVector vector3 = new SharedVector(arr3, VectorOrientation.ROW_MAJOR);
            SharedVector vector4 = new SharedVector(arr4, VectorOrientation.ROW_MAJOR);

            try {
                vector3.add(vector4);
                System.out.println("[FAIL] Dimension Mismatch: Allowed adding different lengths");
            } catch (Exception e) {
                System.out.println("[PASS] Dimension Mismatch: Caught error during addition");
            }
        }
        //test addition during readlock
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> {
                vector1.add(vector2);
            });
            t.start();
            
            // Give some time for the addition to attempt
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                t.interrupt();
            }

            if(t.isAlive()){
                System.out.println("[PASS] Addition during ReadLock: Addition is blocked as expected");
                t.interrupt(); // Clean up the thread
            }
            else{
                System.out.println("[FAIL] Addition during ReadLock: Addition proceeded unexpectedly");
            }
            vector1.readUnlock();
        }
        //test addition during writelock
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            try{
                vector1.add(vector2);
                System.out.println("[FAIL] Addition during WriteLock: No exception thrown");
            }
            catch (Exception e){
                System.out.println("[PASS] Addition during WriteLock: Caught exception");
            }
            finally{
                vector1.writeUnlock();
            }
        }
        //test addition with null vector
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            try{
                vector1.add(nullVector);
                System.out.println("[FAIL] Addition with Null Vector: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Addition with Null Vector: Caught exception");
            }
        }
        //test addition to null vector
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            try{
                nullVector.add(vector1);
                System.out.println("[FAIL] Null Vector Addition: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Null Vector Addition: Caught exception");
            }
        }
        //test addition does not change other vector
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            double[] arr2copy ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector2Expected = new SharedVector(arr2copy, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); //perform simple addition        
            TestUtils.checkObject("Addition Does Not Change Other Vector", vector2, vector2Expected);
        }
        //test addition with different orientations
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            try{
                vector1.add(vector2);
                System.out.println("[FAIL] Addition with Different Orientations: No exception thrown");
            }
            catch (IllegalArgumentException e){
                System.out.println("[PASS] Addition with Different Orientations: Caught exception");
            }
        }
    }

    @Test public void testVectorTranspose(){
        System.out.println("\n-- Testing Vector Transpose --");

        //test transpose of row-major vector
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.transpose();
            
            TestUtils.checkObject("Transpose of Row-Major Vector", vector1.getOrientation(), VectorOrientation.COLUMN_MAJOR);
        }
        //test transpose of column-major vector
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            
            vector1.transpose();
            
            TestUtils.checkObject("Transpose of Column-Major Vector", vector1.getOrientation(), VectorOrientation.ROW_MAJOR);
        }
        //test double transpose returns to original
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.transpose();
            vector1.transpose();
            
            TestUtils.checkObject("Double Transpose Returns Original", vector1, vector1copy);
        }
        //test transpose during readlock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> vector1.transpose());
            t.start();
            // Give some time for the transpose to attempt
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                t.interrupt();
            }
            if(t.isAlive()){
                System.out.println("[PASS] Transpose during ReadLock: Transpose is blocked as expected");
                t.interrupt(); // Clean up the thread
            }
            else{
                System.out.println("[FAIL] Transpose during ReadLock: Transpose proceeded unexpectedly");
            }

            vector1.readUnlock();
        }
        //test transpose during writelock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            try{
                vector1.transpose();
                System.out.println("[FAIL] Transpose during WriteLock: No exception thrown");
            }
            catch (Exception e){
                System.out.println("[PASS] Transpose during WriteLock: Caught exception");
            }
            finally{
                vector1.writeUnlock();
            }
        }
        //test transpose null vector
        {
            SharedVector nullVector=null;
            
            try{
                nullVector.transpose();
                System.out.println("[FAIL] Transpose nullptr failed");
            }
            catch(NullPointerException e){
                System.out.println("[PASS] Transpose nullptr caught");
            }
        }
    }

    @Test public void testVectorNegate(){
        System.out.println("\n-- Testing Vector Negation --");

        //test simple negation
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr1neg ={-1.0,-2.0,-3.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1neg = new SharedVector(arr1neg, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            
            TestUtils.checkObject("Negate Check", vector1, vector1neg);
        }
        //test negation on negative numbers
        {
            double[] arr2 ={ -4.0, 5.0, -6.0 };
            double[] arr2neg ={ 4.0, -5.0, 6.0 };
            
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector2neg = new SharedVector(arr2neg, VectorOrientation.ROW_MAJOR);
            
            vector2.negate();
            
            TestUtils.checkObject("Double Negate Check", vector2, vector2neg);
        }
        //test negation during readlock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> { vector1.negate(); });
            t.start();

            // Give some time for the negation to attempt
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                t.interrupt();
            }
            if(t.isAlive()){
                System.out.println("[PASS] Negation during ReadLock: Negation is blocked as expected");
                t.interrupt(); // Clean up the thread
            }
            else{
                System.out.println("[FAIL] Negation during ReadLock: Negation proceeded unexpectedly");
            }
            
            vector1.readUnlock();
            
        }
        //test negation during writelock
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            try{
                vector1.negate();
                System.out.println("[FAIL] Negation during WriteLock: No exception thrown");
            }
            catch (Exception e){
                System.out.println("[PASS] Negation during WriteLock: Caught exception");
            }
            finally{
                vector1.writeUnlock();
            }
        }
        //test negate null vector
        {
            SharedVector nullVector=null;
            
            try{
                nullVector.negate();
                System.out.println("[FAIL] Negate nullptr failed");
            }
            catch(NullPointerException e){
                System.out.println("[PASS] Negate nullptr caught");
            }
        }
        //test double negation returns to original
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            vector1.negate();
            
            TestUtils.checkObject("Double Negation Returns Original", vector1, vector1copy);
        }
        //test negation of zero
        {
            double[] arr1 ={0.0,0.0,0.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            
            TestUtils.checkObject("Negation of Zero Vector", vector1, vector1copy);
        }
        //test negation of column orientation
        {
            double[] arr1 ={1.0,-2.0,3.0};
            double[] arr1neg ={-1.0,2.0,-3.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector1neg = new SharedVector(arr1neg, VectorOrientation.COLUMN_MAJOR);
            
            vector1.negate();
            
            TestUtils.checkObject("Negation of Column-Major Vector", vector1, vector1neg);
        }
    }

    @Nested
    @DisplayName("Testing Vector Dot Product")
    class testVectorDotProduct{
        //test simple dot product
        @Test public void testVectorDotProductSimple(){
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(32.0, dotProduct,"Simple Dot Product");
        }
        //test dot product with negatives
        @Test public void testVectorDotProductWithNegatives(){
            double[] arr1 ={1.0,-2.0,3.0};
            double[] arr2 ={-4.0,5.0,-6.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(-32.0, dotProduct,"Dot Product with Negatives");
        }
        //test dot product with dimension mismatch
        @Test public void testVectorDotProductDimensionMismatch(){
            double[] arr3 = {1};
            double[] arr4 = {1,2};
            
            SharedVector vector3 = new SharedVector(arr3, VectorOrientation.ROW_MAJOR);
            SharedVector vector4 = new SharedVector(arr4, VectorOrientation.COLUMN_MAJOR);

            assertThrows(IllegalArgumentException.class, () -> {
                double dotProduct = vector3.dot(vector4);
            }, "Expected exception during Dot Product with Dimension Mismatch");    
        }
        //test dot product during readlock
        @Test public void testVectorDotProductDuringReadLock(){
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            vector1.readLock();
            try{
                assertThrows(Exception.class, () -> {
                    double dotProduct = vector1.dot(vector2);
                }, "Dot Product expected exception during ReadLock");
            }
            finally{
                vector1.readUnlock();
            }
        }
        //test dot product during writelock
        @Test public void testVectorDotProductDuringWriteLock(){
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            vector1.writeLock();
            try{
                assertThrows(Exception.class, () -> {
                    double dotProduct = vector1.dot(vector2);
                }, "Dot Product expected exception during WriteLock");
            }
            finally{
                vector1.writeUnlock();
            }
        }
        //test dot product with null vector
        @Test public void testVectorDotProductWithNullVector(){
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            assertThrows(NullPointerException.class, () -> {
                double dotProduct = vector1.dot(nullVector);
            }, "Expected exception during Dot Product with Null Vector");    
        }
        //test dot product to null vector
        @Test public void testVectorDotProductToNullVector(){
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                double dotProduct = nullVector.dot(vector1);
            }, "Expected exception during Dot Product to Null Vector");
        }
        //test dot product with same orientation
        @Test public void testVectorDotProductSameOrientation(){
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertThrows(IllegalArgumentException.class, () -> {
                double dotProduct = vector1.dot(vector2);
            }, "Expected exception during Dot Product with Same Orientation");
        }
        //test dot product with reversed orientations
        @Test public void testVectorDotProductReversedOrientations(){
            double[] arr1 ={1.0,2.0,3.0};
            double[] arr2 ={4.0,5.0,6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            assertThrows(IllegalArgumentException.class, () -> {
                double dotProduct = vector1.dot(vector2);
            }, "Expected exception during Dot Product with Reversed Orientations");
        }
    }

    @Nested
    @DisplayName("Testing Vector-Scalar Multiplication")
    class testVectorScalarMultiplication{
        //test valid multiplication
        @Test public void testVectorScalarMultiplicationValid(){
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            double[] expectedArr ={48.0,54.0,60.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);

            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(expectedVector, vector1, "Valid Vector-Matrix Multiplication");
        }
        //test multiplication with dimension mismatch
        @Test public void testVectorScalarMultiplicationDimensionMismatch() {
            double[] arr1 ={1.0,2.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix(table);
            
            assertThrows(IllegalArgumentException.class, () -> {
                vector1.vecMatMul(matrix);
            }, "Expected exception during Vector-Matrix Multiplication with Dimension Mismatch");
        }
        //test wrong vector orientation
        @Test public void testVectorScalarMultiplicationWrongOrientation(){
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            assertThrows(UnsupportedOperationException.class, () -> {
                vector1.vecMatMul(matrix);
            }, "Expected exception during Vector-Matrix Multiplication with Wrong Vector Orientation");
        }
        //test wrong matrix orientation
        @Test public void testVectorScalarMultiplicationWrongMatrixOrientation(){
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix(table); //row-major
            
            assertThrows(UnsupportedOperationException.class, () -> {
                vector1.vecMatMul(matrix);
            }, "Expected exception during Vector-Matrix Multiplication with Wrong Matrix Orientation");    
        }
        //test multiplication during readlock
        @Test public void testVectorScalarMultiplicationDuringReadLock(){
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            vector1.readLock();
            Thread t = new Thread(() -> { vector1.vecMatMul(matrix); });
            t.start();
            
            // Give some time for the multiplication to attempt
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                t.interrupt();
            }
            try{
                assertTrue(t.isAlive(), "Vector-Matrix Multiplication during ReadLock: Multiplication is blocked as expected");
            } finally{
                t.interrupt(); // Clean up the thread
                vector1.readUnlock();
            }
        }
        //test multiplication during writelock
        @Test public void testVectorScalarMultiplicationDuringWriteLock(){
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            vector1.writeLock();
            
            try {
                assertThrows(Exception.class, () -> {
                    vector1.vecMatMul(matrix);
                }, "Vector-Matrix Multiplication during WriteLock: Expected exception thrown");
            } finally {
                vector1.writeUnlock();
            }
        }
        //test multiplication with null vector
        {
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            SharedVector nullVector = null;
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            try{
                nullVector.vecMatMul(matrix);
                System.out.println("[FAIL] Vector-Matrix Multiplication with Null Vector: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Vector-Matrix Multiplication with Null Vector: Caught exception");
            }
        }
        //test multiplication with null matrix
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix nullMatrix = null;
            
            try{
                vector1.vecMatMul(nullMatrix);
                System.out.println("[FAIL] Vector-Matrix Multiplication with Null Matrix: No exception thrown");
            }
            catch (NullPointerException e){
                System.out.println("[PASS] Vector-Matrix Multiplication with Null Matrix: Caught exception");
            }
        }
        //test multiplication by identity matrix
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[][] identityTable = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix identityMatrix = new SharedMatrix();
            identityMatrix.loadColumnMajor(identityTable);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(identityMatrix);
            
            TestUtils.checkObject("Multiplication by Identity Matrix", vector1, vector1copy);
        }
        //test multiplication by zero matrix
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[][] zeroTable = {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
            };
            double[] expectedArr ={0.0,0.0,0.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix zeroMatrix = new SharedMatrix();
            zeroMatrix.loadColumnMajor(zeroTable);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(zeroMatrix);
            
            TestUtils.checkObject("Multiplication by Zero Matrix", vector1, expectedVector);
        }
        //test multiplication resulting in negative values
        {
            double[] arr1 ={-1.0,-2.0,-3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0,11.0,12.0}
            };
            double[] expectedArr ={-48.0, -54.0, -60.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            TestUtils.checkObject("Vector-Matrix Multiplication with Negatives", vector1, expectedVector);
        }
        //test multiplication with non-square matrix
        {
            double[] arr1 ={1.0,2.0, 3.0};
            double[][] table = {
                {4.0, 5.0},
                {6.0, 7.0},
                {8.0, 9.0}
            };
            double[] expectedArr ={40.0, 46.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.COLUMN_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            TestUtils.checkObject("Vector-Matrix Multiplication with Non-Square Matrix", vector1, expectedVector);
        }
        //test tiny multiplication 1x1
        {
            double[] arr1 ={3.0};
            double[][] table = {
                {4.0}
            };
            double[] expectedArr ={12.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            TestUtils.checkObject("Tiny Vector-Matrix Multiplication 1x1", vector1, expectedVector);
        }
        //test changed dimensions after multiplication
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {4.0, 5.0},
                {6.0, 7.0},
                {8.0, 9.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            vector1.vecMatMul(matrix);
            
            TestUtils.checkObject("Dimensions After Vector-Matrix Multiplication", vector1.length(), 2);
        }
        //test multiplication a single row matrix
        {
            double[] arr1 ={1.0,2.0,3.0};
            double[][] table = {
                {4.0}, 
                {5.0}, 
                {6.0}
            };
            double[] expectedArr ={32.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            TestUtils.checkObject("Vector-Matrix Multiplication with Single Row Matrix", vector1, expectedVector);
        }
        //test multiplication with an empty matrix
        {
            double[] arr1 ={1.0,2.0,3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix(); //empty matrix
            
            try{
                vector1.vecMatMul(matrix);
                System.out.println("[FAIL] Vector-Matrix Multiplication with Empty Matrix: No exception thrown");
            }
            catch (UnsupportedOperationException e){
                System.out.println("[PASS] Vector-Matrix Multiplication with Empty Matrix: Caught exception");
            }
        }
    }


}
