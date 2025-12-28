import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import memory.SharedMatrix;
import memory.SharedVector;
import memory.VectorOrientation;

public class TestVector {
    @Nested
    @DisplayName("Vector Creation Tests")
    class vectorCreationTests {
         //test valid vector creation row-major
        @Test public void testBasicVectorCreation() {

            double[] arr ={1.0,2.0,3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            //check orientation match
            assertEquals(VectorOrientation.ROW_MAJOR, testVector.getOrientation(), "Wrong Vector Orientation, Expected Row Major");
            
            //check length match
            assertEquals(3,testVector.length(),"Wrong vector length recieved");
        

            //check values match
            assertEquals(1.0, testVector.get(0), "Wrong value at index 0, after creation");
            assertEquals(2.0, testVector.get(1), "Wrong value at index 1, after creation");
            assertEquals(3.0, testVector.get(2), "Wrong value at index 2, after creation");
        }
        //test valid vector creation column-major
        @Test public void testColumnMajorVectorCreation() {
            double[] arr ={4.0,5.0,6.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.COLUMN_MAJOR);
            
            //check orientation match
            assertEquals(VectorOrientation.COLUMN_MAJOR, testVector.getOrientation(), "Wrong Vector Orientation, Expected Column Major");
            
            //check length match
            assertEquals(3, testVector.length(), "Wrong vector length received");

            //check values match
            assertEquals(4.0, testVector.get(0), "Wrong value at index 0");
            assertEquals(5.0, testVector.get(1), "Wrong value at index 1");
            assertEquals(6.0, testVector.get(2), "Wrong value at index 2");
        }
        //test null orientation
        @Test public void testNullOrientation() {
            double[] arr = {1.0, 2.0, 3.0};
            assertThrows(IllegalArgumentException.class, () -> {
                SharedVector brokenVector = new SharedVector(arr, null);
            }, "Expected IllegalArgumentException for null orientation");
        }
        //test empty vector creation
        @Test public void testEmptyVectorCreation() {
            double[] emptyArr = new double[0];
            assertThrows(IllegalArgumentException.class, () -> {
                SharedVector brokenVector = new SharedVector(emptyArr, VectorOrientation.ROW_MAJOR);
            }, "Expected IllegalArgumentException for empty array");
        }
        //test empty vector creation different option
        @Test public void testEmptyVectorCreation2() {
            double[] emptyArr2 = {};
            assertThrows(IllegalArgumentException.class, () -> {
                SharedVector brokenVector2 = new SharedVector(emptyArr2, VectorOrientation.COLUMN_MAJOR);
            }, "Expected IllegalArgumentException for empty array literal");
        }
        //test null vector creation
        @Test public void testNullVectorCreation() {
            assertThrows(IllegalArgumentException.class, () -> {
                SharedVector nullVector = new SharedVector(null, VectorOrientation.ROW_MAJOR);
            }, "Expected IllegalArgumentException for null vector");
        }
        //test deep copy of vector during creation
        @Test public void testDeepCopy() {
            double[] deepCopyArr = {1.0, 2.0, 3.0};
            SharedVector testVector = new SharedVector(deepCopyArr, VectorOrientation.ROW_MAJOR);
            
            // Modify the original source array
            deepCopyArr[0] = 999.0;
            
            // Check that the vector remains unchanged (deep copy was made)
            assertEquals(1.0, testVector.get(0), "Vector should not be affected by changes to source array");
        }
    }   
    
    @Nested
    @DisplayName("Vector Get Function Tests")
    class vectorGetFunctionTests {
        // Test get null vector
        @Test public void testGetOnNullVector() {
            SharedVector nullVector = null;
            assertThrows(NullPointerException.class, () -> {
                nullVector.get(0);
            }, "Expected NullPointerException when calling get on null vector");
        }
        // Test get negative index
        @Test public void testGetNegativeIndex() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            assertThrows(IndexOutOfBoundsException.class, () -> {
                testVector.get(-1);
            }, "Expected IndexOutOfBoundsException for negative index");
        }
        // Test get out-of-bounds index
        @Test public void testGetOutOfBoundsIndex() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            assertThrows(IndexOutOfBoundsException.class, () -> {
                testVector.get(3);
            }, "Expected IndexOutOfBoundsException for out-of-bounds index");
        }
        // Test get during readlock
        @Test public void testGetDuringReadLock() {
        double[] arr = {1.0, 2.0, 3.0};
        SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
        
        testVector.readLock();
        try {
            double val = testVector.get(1);
            assertEquals(2.0, val, "Get should work correctly during read lock");
        } finally {
            testVector.readUnlock();
        }
    }
        // Test get during writelock
        @Test public void testGetDuringWriteLock() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            testVector.writeLock();
            try {
                double val = testVector.get(1);
                assertEquals(2.0, val, "Get should work correctly during write lock");
            } finally {
                testVector.writeUnlock();
            }
        }
        // Test valid get operation
        @Test public void testValidGetOperation() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector testVector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            // Should print "1.0 2.0 3.0 "
            String str = "";
            for (int i = 0; i < testVector.length(); i++) {
                str += (testVector.get(i)) + " ";
            }
            
            assertEquals("1.0 2.0 3.0 ", str, "Vector Get should return correct values");
        }
    }
    
    @Nested
    @DisplayName("Vector Length Function Tests")
    class vectorLengthTests {
       // Test length of valid vector
        @Test public void testLengthOfValidVector() {
            double[] arr1 = {1.0, 2.0, 3.0, 4.0, 5.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            int length = vector1.length();
            
            assertEquals(5, length, "Valid Vector Length should be 5");
        }
        // Test length of null vector
        @Test public void testLengthOfNullVector() {
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                int length = nullVector.length();
            }, "Expected NullPointerException when calling length on null vector");
        }
        // Test length during readlock
        @Test public void testLengthDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            try {
                int length = vector1.length();
                assertEquals(3, length, "Length should work correctly during read lock");
            } finally {
                vector1.readUnlock();
            }
        }
        // Test length during writelock
        @Test public void testLengthDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            try {
                int length = vector1.length();
                assertEquals(3, length, "Length should work correctly during write lock");
            } finally {
                vector1.writeUnlock();
            }
        } 
    }

    @Nested
    @DisplayName("Vector Add Tests")
    class vectorAddTests {
        // Test simple addition
        @Test public void testSimpleAddition() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            double[] arrsum1 = {5.0, 7.0, 9.0};
            SharedVector sumVector1 = new SharedVector(arrsum1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); // Perform simple addition
            assertEquals(sumVector1, vector1, "Vector Simple Addition");
        }
        // Test self addition
        @Test public void testSelfAddition() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arrsum2 = {2.0, 4.0, 6.0};
            SharedVector sumVector2 = new SharedVector(arrsum2, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector1); // Perform self addition
            assertEquals(sumVector2, vector1, "Vector Self Addition");
        }
        // Test addition with negative numbers
        @Test public void testAdditionWithNegatives() {
            double[] arr1 = {1.0, -2.0, 3.0};
            double[] arr2 = {-4.0, 5.0, -6.0};
            double[] arrsum3 = {-3.0, 3.0, -3.0};
            SharedVector sumVector3 = new SharedVector(arrsum3, VectorOrientation.ROW_MAJOR);
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); // Perform addition with negatives
            assertEquals(sumVector3, vector1, "Vector Addition with Negatives");
        }
        // Test addition with dimension mismatch
        @Test public void testAdditionDimensionMismatch() {
            double[] arr3 = {1};
            double[] arr4 = {1, 2};
            
            SharedVector vector3 = new SharedVector(arr3, VectorOrientation.ROW_MAJOR);
            SharedVector vector4 = new SharedVector(arr4, VectorOrientation.ROW_MAJOR);

            assertThrows(IllegalArgumentException.class, () -> {
                vector3.add(vector4);
            }, "Expected exception for dimension mismatch");
        }
        // Test addition during readlock
        @Test public void testAdditionDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);

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
            //make sure the add attempt failed, hence the vector remained unchanged
            assertEquals(vector1Expected, vector1, "Addition during ReadLock: Vector should remain unchanged");
            
            vector1.readUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test addition during writelock
        // Test addition during writelock
        @Test public void testAdditionDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            Thread t = new Thread(() -> {
                vector1.add(vector2);
            });
            t.start();
            
            // Give some time for the addition to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }

            // Check that vector1 remains unchanged (addition was blocked)
            assertEquals(vector1Expected, vector1, "Addition during WriteLock: Vector should remain unchanged");
            
            vector1.writeUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test addition with null vector
        @Test public void testAdditionWithNullVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                vector1.add(nullVector);
            }, "Expected NullPointerException for null vector");
        }
        // Test addition to null vector
        @Test public void testAdditionToNullVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                nullVector.add(vector1);
            }, "Expected NullPointerException when adding to null vector");
        }
        // Test addition does not change other vector
        @Test public void testAdditionDoesNotChangeOtherVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            double[] arr2copy = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector2Expected = new SharedVector(arr2copy, VectorOrientation.ROW_MAJOR);
            
            vector1.add(vector2); // Perform simple addition
            assertEquals(vector2Expected, vector2, "Addition Does Not Change Other Vector");
        }
        // Test addition with different orientations
        @Test public void testAdditionWithDifferentOrientations() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            assertThrows(IllegalArgumentException.class, () -> {
                vector1.add(vector2);
            }, "Expected IllegalArgumentException for different orientations");
    }
    }
    
    @Nested
    @DisplayName("Vector Transpose Tests")
    class vectorTransposeTests {
        // Test transpose of row-major vector
        @Test public void testTransposeOfRowMajorVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.transpose();
            
            assertEquals(VectorOrientation.COLUMN_MAJOR, vector1.getOrientation(), "Transpose of Row-Major Vector");
        }
        // Test transpose of column-major vector
        @Test public void testTransposeOfColumnMajorVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            
            vector1.transpose();
            
            assertEquals(VectorOrientation.ROW_MAJOR, vector1.getOrientation(), "Transpose of Column-Major Vector");
        }
        // Test double transpose returns to original
        @Test public void testDoubleTransposeReturnsOriginal() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.transpose();
            vector1.transpose();
            
            assertEquals(vector1copy, vector1, "Double Transpose Returns Original");
        }
        // Test transpose during readlock
        @Test public void testTransposeDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> vector1.transpose());
            t.start();
            
            // Give some time for the transpose to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (transpose was blocked)
            assertEquals(vector1Expected, vector1, "Transpose during ReadLock: Vector should remain unchanged");
            
            vector1.readUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test transpose during writelock
        @Test public void testTransposeDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            Thread t = new Thread(() -> vector1.transpose());
            t.start();
            
            // Give some time for the transpose to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (transpose was blocked)
            assertEquals(vector1Expected, vector1, "Transpose during WriteLock: Vector should remain unchanged");
            
            vector1.writeUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test transpose null vector
        @Test public void testTransposeNullVector() {
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                nullVector.transpose();
            }, "Expected NullPointerException when transposing null vector");
        }
    }
    
    @Nested
    @DisplayName("Vector Dot Product Tests")
    class testVectorDotProduct{
        // Test simple dot product
        @Test public void testVectorDotProductSimple() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(32.0, dotProduct, "Simple Dot Product");
        }
        // Test dot product with negatives
        @Test public void testVectorDotProductWithNegatives() {
            double[] arr1 = {1.0, -2.0, 3.0};
            double[] arr2 = {-4.0, 5.0, -6.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(-32.0, dotProduct, "Dot Product with Negatives");
        }
        // Test dot product with dimension mismatch
        @Test public void testVectorDotProductDimensionMismatch() {
            double[] arr3 = {1};
            double[] arr4 = {1, 2};
            
            SharedVector vector3 = new SharedVector(arr3, VectorOrientation.ROW_MAJOR);
            SharedVector vector4 = new SharedVector(arr4, VectorOrientation.COLUMN_MAJOR);

            assertThrows(IllegalArgumentException.class, () -> {
                double dotProduct = vector3.dot(vector4);
            }, "Expected exception during Dot Product with Dimension Mismatch");
        }
        // Test dot product during readlock
        @Test public void testVectorDotProductDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> {
                double dotProduct = vector1.dot(vector2);
            });
            t.start();
            
            // Give some time for the dot product to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (dot product was blocked or doesn't modify)
            assertEquals(vector1Expected, vector1, "Dot Product during ReadLock: Vector should remain unchanged");
            
            vector1.readUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test dot product during writelock
        @Test public void testVectorDotProductDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            Thread t = new Thread(() -> {
                double dotProduct = vector1.dot(vector2);
            });
            t.start();
            
            // Give some time for the dot product to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (dot product was blocked or doesn't modify)
            assertEquals(vector1Expected, vector1, "Dot Product during WriteLock: Vector should remain unchanged");
            
            vector1.writeUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test dot product with null vector
        @Test public void testVectorDotProductWithNullVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                double dotProduct = vector1.dot(nullVector);
            }, "Expected exception during Dot Product with Null Vector");
        }
        // Test dot product to null vector
        @Test public void testVectorDotProductToNullVector() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                double dotProduct = nullVector.dot(vector1);
            }, "Expected exception during Dot Product to Null Vector");
        }
        // Test dot product with same orientation
        @Test public void testVectorDotProductSameOrientation() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertThrows(UnsupportedOperationException.class, () -> {
                double dotProduct = vector1.dot(vector2);
            }, "Expected exception during Dot Product with Same Orientation");
        }
        // Test dot product with reversed orientations
        @Test public void testVectorDotProductReversedOrientations() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertThrows(UnsupportedOperationException.class, () -> {
                double dotProduct = vector1.dot(vector2);
            }, "Expected exception during Dot Product with Reversed Orientations");
        }
        // Test dot product with zeros
        @Test public void testVectorDotProductWithZeros() {
            double[] arr1 = {0.0, 0.0, 0.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(0.0, dotProduct, "Dot Product with Zero Vector");
        }
        // Test dot product of single element vectors
        @Test public void testVectorDotProductSingleElement() {
            double[] arr1 = {5.0};
            double[] arr2 = {3.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            double dotProduct = vector1.dot(vector2);
            
            assertEquals(15.0, dotProduct, "Dot Product Single Element");
        }
    }

    @Nested
    @DisplayName("Vector Negate Tests")
    class testVectorNegate{
        // Test simple negation
        @Test public void testSimpleNegation() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr1neg = {-1.0, -2.0, -3.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1neg = new SharedVector(arr1neg, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            
            assertEquals(vector1neg, vector1, "Negate Check");
        }
        // Test negation on negative numbers
        @Test public void testNegationOnNegativeNumbers() {
            double[] arr2 = {-4.0, 5.0, -6.0};
            double[] arr2neg = {4.0, -5.0, 6.0};
            
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            SharedVector vector2neg = new SharedVector(arr2neg, VectorOrientation.ROW_MAJOR);
            
            vector2.negate();
            
            assertEquals(vector2neg, vector2, "Double Negate Check");
        }
        // Test negation during readlock
        @Test public void testNegationDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> { vector1.negate(); });
            t.start();

            // Give some time for the negation to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (negation was blocked)
            assertEquals(vector1Expected, vector1, "Negation during ReadLock: Vector should remain unchanged");
            
            vector1.readUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test negation during writelock
        @Test public void testNegationDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            Thread t = new Thread(() -> { vector1.negate(); });
            t.start();
            
            // Give some time for the negation to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (negation was blocked)
            assertEquals(vector1Expected, vector1, "Negation during WriteLock: Vector should remain unchanged");
            
            vector1.writeUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test negate null vector
        @Test public void testNegateNullVector() {
            SharedVector nullVector = null;
            
            assertThrows(NullPointerException.class, () -> {
                nullVector.negate();
            }, "Expected NullPointerException when negating null vector");
        }
        // Test double negation returns to original
        @Test public void testDoubleNegationReturnsOriginal() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            vector1.negate();
            
            assertEquals(vector1copy, vector1, "Double Negation Returns Original");
        }
        // Test negation of zero
        @Test public void testNegationOfZero() {
            double[] arr1 = {0.0, 0.0, 0.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector1copy = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            
            vector1.negate();
            
            assertEquals(vector1copy, vector1, "Negation of Zero Vector");
        }
        // Test negation of column orientation
        @Test public void testNegationOfColumnOrientation() {
            double[] arr1 = {1.0, -2.0, 3.0};
            double[] arr1neg = {-1.0, 2.0, -3.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.COLUMN_MAJOR);
            SharedVector vector1neg = new SharedVector(arr1neg, VectorOrientation.COLUMN_MAJOR);
            
            vector1.negate();
            
            assertEquals(vector1neg, vector1, "Negation of Column-Major Vector");
        }
    }

    @Nested
    @DisplayName("Vector-Scalar Multiplication Tests")
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
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
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
        // Test multiplication during readlock
        @Test public void testVectorScalarMultiplicationDuringReadLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0, 11.0, 12.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.readLock();
            Thread t = new Thread(() -> { vector1.vecMatMul(matrix); });
            t.start();
            
            // Give some time for the multiplication to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (multiplication was blocked)
            assertEquals(vector1Expected, vector1, "Vector-Matrix Multiplication during ReadLock: Vector should remain unchanged");
            
            vector1.readUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test multiplication during writelock
        @Test public void testVectorScalarMultiplicationDuringWriteLock() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0, 11.0, 12.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector vector1Expected = new SharedVector(new double[]{1.0, 2.0, 3.0}, VectorOrientation.ROW_MAJOR);
            
            vector1.writeLock();
            Thread t = new Thread(() -> { vector1.vecMatMul(matrix); });
            t.start();
            
            // Give some time for the multiplication to attempt
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                t.interrupt();
            }
            
            // Check that vector1 remains unchanged (multiplication was blocked)
            assertEquals(vector1Expected, vector1, "Vector-Matrix Multiplication during WriteLock: Vector should remain unchanged");
            
            vector1.writeUnlock();
            t.interrupt(); // Clean up the thread
        }
        // Test multiplication with null vector
        @Test public void testVectorScalarMultiplicationWithNullVector() {
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0, 11.0, 12.0}
            };
            SharedVector nullVector = null;
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            assertThrows(NullPointerException.class, () -> {
                nullVector.vecMatMul(matrix);
            }, "Expected NullPointerException for null vector");
        }
        // Test multiplication with null matrix
        @Test public void testVectorScalarMultiplicationWithNullMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix nullMatrix = null;
            
            assertThrows(NullPointerException.class, () -> {
                vector1.vecMatMul(nullMatrix);
            }, "Expected NullPointerException for null matrix");
        }
        // Test multiplication by identity matrix
        @Test public void testVectorScalarMultiplicationByIdentityMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
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
            
            assertEquals(vector1copy, vector1, "Multiplication by Identity Matrix");
        }
        // Test multiplication by zero matrix
        @Test public void testVectorScalarMultiplicationByZeroMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] zeroTable = {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
            };
            double[] expectedArr = {0.0, 0.0, 0.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix zeroMatrix = new SharedMatrix();
            zeroMatrix.loadColumnMajor(zeroTable);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(zeroMatrix);
            
            assertEquals(expectedVector, vector1, "Multiplication by Zero Matrix");
        }
        // Test multiplication resulting in negative values
        @Test public void testVectorScalarMultiplicationWithNegatives() {
            double[] arr1 = {-1.0, -2.0, -3.0};
            double[][] table = {
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {10.0, 11.0, 12.0}
            };
            double[] expectedArr = {-48.0, -54.0, -60.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(expectedVector, vector1, "Vector-Matrix Multiplication with Negatives");
        }
        // Test multiplication with non-square matrix
        @Test public void testVectorScalarMultiplicationWithNonSquareMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] table = {
                {4.0, 5.0},
                {6.0, 7.0},
                {8.0, 9.0}
            };
            double[] expectedArr = {40.0, 46.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(expectedVector, vector1, "Vector-Matrix Multiplication with Non-Square Matrix");
        }
        // Test tiny multiplication 1x1
        @Test public void testVectorScalarMultiplicationTiny1x1() {
            double[] arr1 = {3.0};
            double[][] table = {
                {4.0}
            };
            double[] expectedArr = {12.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(expectedVector, vector1, "Tiny Vector-Matrix Multiplication 1x1");
        }
        // Test changed dimensions after multiplication
        @Test public void testVectorScalarMultiplicationChangedDimensions() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] table = {
                {4.0, 5.0},
                {6.0, 7.0},
                {8.0, 9.0}
            };
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(2, vector1.length(), "Dimensions After Vector-Matrix Multiplication");
        }
        // Test multiplication a single row matrix
        @Test public void testVectorScalarMultiplicationWithSingleRowMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[][] table = {
                {4.0}, 
                {5.0}, 
                {6.0}
            };
            double[] expectedArr = {32.0};
            
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix();
            matrix.loadColumnMajor(table);
            SharedVector expectedVector = new SharedVector(expectedArr, VectorOrientation.ROW_MAJOR);
            
            vector1.vecMatMul(matrix);
            
            assertEquals(expectedVector, vector1, "Vector-Matrix Multiplication with Single Row Matrix");
        }
        // Test multiplication with an empty matrix
        @Test public void testVectorScalarMultiplicationWithEmptyMatrix() {
            double[] arr1 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedMatrix matrix = new SharedMatrix(); // Empty matrix
            
            assertThrows(IllegalArgumentException.class, () -> {
                vector1.vecMatMul(matrix);
            }, "Expected IllegalArgumentException for empty matrix");
        }
    }

    @Nested
    @DisplayName("Vector Equals Tests")
    class vectorEqualsTests {
        // Test equals with same vector
        @Test public void testEqualsSameVector() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector vector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            assertEquals(vector, vector, "Vector should equal itself");
        }        
        // Test equals with identical vectors
        @Test public void testEqualsIdenticalVectors() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertEquals(vector1, vector2, "Identical vectors should be equal");
        }        
        // Test equals with different values
        @Test public void testEqualsWithDifferentValues() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {4.0, 5.0, 6.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertNotEquals(vector1, vector2, "Vectors with different values should not be equal");
        }        
        // Test equals with different orientations
        @Test public void testEqualsWithDifferentOrientations() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {1.0, 2.0, 3.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.COLUMN_MAJOR);
            
            assertNotEquals(vector1, vector2, "Vectors with different orientations should not be equal");
        }        
        // Test equals with null
        @Test public void testEqualsWithNull() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector vector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            assertNotEquals(null, vector, "Vector should not equal null");
        }
        // Test equals with different lengths
        @Test public void testEqualsWithDifferentLengths() {
            double[] arr1 = {1.0, 2.0, 3.0};
            double[] arr2 = {1.0, 2.0};
            SharedVector vector1 = new SharedVector(arr1, VectorOrientation.ROW_MAJOR);
            SharedVector vector2 = new SharedVector(arr2, VectorOrientation.ROW_MAJOR);
            
            assertNotEquals(vector1, vector2, "Vectors with different lengths should not be equal");
        }
    }

    @Nested
    @DisplayName("Vector ToString Tests")
    class vectorToStringTests {
        // Test toString for row-major vector
        @Test public void testToStringRowMajor() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector vector = new SharedVector(arr, VectorOrientation.ROW_MAJOR);
            
            String result = vector.toString();
            
            assertEquals("[1.0 2.0 3.0]", result, "Row-major vector toString format");
        }
        // Test toString for column-major vector
        @Test public void testToStringColumnMajor() {
            double[] arr = {1.0, 2.0, 3.0};
            SharedVector vector = new SharedVector(arr, VectorOrientation.COLUMN_MAJOR);
            
            String result = vector.toString();
            
            assertEquals("| 1.0 | 2.0 | 3.0 |", result, "Column-major vector toString format");
        }
    }
}
