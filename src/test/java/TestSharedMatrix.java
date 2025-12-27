import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import memory.SharedMatrix;
import memory.SharedVector;
import memory.VectorOrientation;

public class TestSharedMatrix {

    @Nested
    @DisplayName("Matrix Row Major Load Tests")
    class MatrixRowMajorLoadTests {
        //test normal matrix
        @Test public void testNormalMatrixRowMajorLoad() {
            double[][] table = { {1.1,2.2}, {3,4} };
            SharedMatrix matrix = new SharedMatrix(); 

            matrix.loadRowMajor(table);
            assertEquals(VectorOrientation.ROW_MAJOR, matrix.getOrientation(),  "Matrix Row Major Orientation");
            assertEquals(2.2,matrix.get(0).get(1), "Matrix Row Major Content");
        }
        //test tiny matrix
        @Test public void testTinyMatrixRowMajorLoad() {
            double[][] tinyTable = {{4.0}};
            SharedMatrix tinyMatrix = new SharedMatrix();
            
            tinyMatrix.loadRowMajor(tinyTable);
            assertEquals(VectorOrientation.ROW_MAJOR, tinyMatrix.getOrientation(),  "Tiny Matrix Row Major Orientation");
            assertEquals(4.0, tinyMatrix.get(0).get(0), "Tiny Matrix Row Major Content");
        }
        //test empty matrix
        @Test public void testEmptyMatrixRowMajorLoad() {
            double[][] emptyTable = {{}};
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                emptyMatrix.loadRowMajor(emptyTable);
            }   , "Expected loadRowMajor() to throw, but it didn't");
        }
        //test null matrix
        @Test public void testNullMatrixRowMajorLoad() {
            SharedMatrix nullMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                nullMatrix.loadRowMajor(null); 
            }    , "Expected loadRowMajor() to throw, but it didn't");
        }
        //test uneven matrix
        @Test public void testUnevenMatrixRowMajorLoad() {
            double[][] jaggedTable = { {1,2,3}, {4,5} };
            SharedMatrix jaggedMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                jaggedMatrix.loadRowMajor(jaggedTable);
            }   , "Expected loadRowMajor() to throw, but it didn't");
        }
        //test single row matrix
        @Test public void testSingleRowMatrixRowMajorLoad() {
            double[][] lineTable = { {7,8,9} };
            SharedMatrix lineMatrix = new SharedMatrix();

            lineMatrix.loadRowMajor(lineTable);
            assertEquals(VectorOrientation.ROW_MAJOR, lineMatrix.getOrientation(),  "Single Row Matrix Row Major Orientation");
            assertEquals(9.0, lineMatrix.get(0).get(2), "Single Row Matrix Row Major Content");    
        }
        //test single column matrix
        @Test public void testSingleColumnMatrixRowMajorLoad() {
            double[][] colTable = { {5}, {6}, {7} };
            SharedMatrix colMatrix = new SharedMatrix();

            colMatrix.loadRowMajor(colTable);
            assertEquals(VectorOrientation.ROW_MAJOR, colMatrix.getOrientation(), "Single Column Matrix Row Major Orientation");
            assertEquals(7.0, colMatrix.get(2).get(0), "Single Column Matrix Row Major Content");    
        }
        //test large matrix
        @Test public void testLargeMatrixRowMajorLoad() {
            int rows = 100;
            int cols = 100;
            double[][] largeTable = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    largeTable[i][j] = i * cols + j;
                }
            }
            SharedMatrix largeMatrix = new SharedMatrix();

            largeMatrix.loadRowMajor(largeTable);
            assertEquals(VectorOrientation.ROW_MAJOR, largeMatrix.getOrientation(), "Large Matrix Row Major Orientation");
            assertEquals(9999.0, largeMatrix.get(99).get(99), "Large Matrix Row Major Content");    
        }
        //test matrix with negative values
        @Test public void testNegativeValuesMatrixRowMajorLoad() {
            double[][] negTable = { {-1,-2}, {-3,-4} };
            SharedMatrix negMatrix = new SharedMatrix();

            negMatrix.loadRowMajor(negTable);
            assertEquals(VectorOrientation.ROW_MAJOR, negMatrix.getOrientation(), "Negative Values Matrix Row Major Orientation");
            assertEquals(-4.0, negMatrix.get(1).get(1), "Negative Values Matrix Row Major Content");    
        }
        //test load overwriting old data
        @Test public void testLoadOverwriteRowMajor() {
            double[][] firstTable = { {1,2}, {3,4} };
            double[][] secondTable = { {5,6,7}, {8,9,10}, {11,12,13} };
            SharedMatrix matrix = new SharedMatrix();

            matrix.loadRowMajor(firstTable);
            matrix.loadRowMajor(secondTable);
            assertEquals(VectorOrientation.ROW_MAJOR, matrix.getOrientation(), "Overwrite Load Matrix Row Major Orientation");
            assertEquals(13.0, matrix.get(2).get(2), "Overwrite Load Matrix Row Major Content");    
        }
        //test multiple loads with different orientations
        @Test public void testMultipleLoadsDifferentOrientations() {
            double[][] table = { {1,2,3}, {4,5,6} };
            SharedMatrix matrix = new SharedMatrix();

            matrix.loadRowMajor(table);
            table = matrix.readRowMajor();
            matrix.loadColumnMajor(table);

            assertEquals(VectorOrientation.COLUMN_MAJOR, matrix.getOrientation(), "Transpose Load Matrix Row Major Orientation");
            assertEquals(4.0, matrix.get(0).get(1), "Transpose Load Matrix Row Major Content");    
        }
    }
    
    @Nested
    @DisplayName("Matrix Column Major Load Tests")
    class MatrixColumnMajorLoadTests {
        //test normal matrix
        @Test public void testNormalMatrixColumnMajorLoad() {
            double[][] table = { {1,2}, {3,4} };
            SharedMatrix matrix = new SharedMatrix(); 
            matrix.loadColumnMajor(table);

            double[] arr = {1.0,3.0};
            SharedVector firstCol = new SharedVector(arr,VectorOrientation.COLUMN_MAJOR);

            assertEquals(VectorOrientation.COLUMN_MAJOR, matrix.getOrientation(), "Matrix Column Major Orientation");
            assertEquals(firstCol, matrix.get(0), "Matrix Column Major Content");
        }
        //test tiny matrix
        @Test public void testTinyMatrixColumnMajorLoad() {
            double[][] tinyTable = {{4.0}};
            SharedMatrix tinyMatrix = new SharedMatrix();
            
            tinyMatrix.loadColumnMajor(tinyTable);
            assertEquals(VectorOrientation.COLUMN_MAJOR, tinyMatrix.getOrientation(), "Tiny Matrix Column Major Orientation");
            assertEquals(4.0, tinyMatrix.get(0).get(0), "Tiny Matrix Column Major Content");
        }
        //test empty matrix
        @Test public void testEmptyMatrixColumnMajorLoad() {
            double[][] emptyTable = {{}};
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                emptyMatrix.loadColumnMajor(emptyTable);
            }, "Expected loadColumnMajor() to throw, but it didn't");
        }
        //test null matrix
        @Test public void testNullMatrixColumnMajorLoad() {
            SharedMatrix nullMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                nullMatrix.loadColumnMajor(null);
            }, "Expected loadColumnMajor() to throw, but it didn't");    
        }
        //test uneven matrix
        @Test public void testUnevenMatrixColumnMajorLoad() {
            double[][] jaggedTable = { {1,2,3}, {4,5} };
            SharedMatrix jaggedMatrix = new SharedMatrix();
            assertThrows(IllegalArgumentException.class, () -> {
                jaggedMatrix.loadColumnMajor(jaggedTable);
            }, "Expected loadColumnMajor() to throw, but it didn't");   
        }
        //test single row matrix
        @Test public void testSingleRowMatrixColumnMajorLoad() {
            double[][] lineTable = { {7,8,9} };
            SharedMatrix lineMatrix = new SharedMatrix();

            lineMatrix.loadColumnMajor(lineTable);
            assertEquals(VectorOrientation.COLUMN_MAJOR, lineMatrix.getOrientation(), "Single Row Matrix Column Major Orientation");
            assertEquals(9.0, lineMatrix.get(2).get(0), "Single Row Matrix Column Major Content");    
        }
        //test single column matrix
        @Test public void testSingleColumnMatrixColumnMajorLoad() {
            double[][] colTable = { {5}, 
                                    {6}, 
                                    {7} };
            SharedMatrix colMatrix = new SharedMatrix();

            colMatrix.loadColumnMajor(colTable);
            assertEquals(VectorOrientation.COLUMN_MAJOR, colMatrix.getOrientation(), "Single Column Matrix Column Major Orientation");
            assertEquals(6.0, colMatrix.get(0).get(1), "Single Column Matrix Column Major Content");    
        }
        //test large matrix
        @Test public void testLargeMatrixColumnMajorLoad() {
            int rows = 100;
            int cols = 100;
            double[][] largeTable = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    largeTable[i][j] = i * cols + j;
                }
            }
            SharedMatrix largeMatrix = new SharedMatrix();

            largeMatrix.loadColumnMajor(largeTable);
            assertEquals(VectorOrientation.COLUMN_MAJOR, largeMatrix.getOrientation(), "Large Matrix Column Major Orientation");
            assertEquals(9999.0, largeMatrix.get(99).get(99), "Large Matrix Column Major Content");    
        }
        //test matrix with negative values
        @Test public void testNegativeValuesMatrixColumnMajorLoad() {
            double[][] negTable = { {-1,-2}, {-3,-4} };
            SharedMatrix negMatrix = new SharedMatrix();

            negMatrix.loadColumnMajor(negTable);
            assertEquals(VectorOrientation.COLUMN_MAJOR, negMatrix.getOrientation(), "Negative Values Matrix Column Major Orientation");
            assertEquals(-4.0, negMatrix.get(1).get(1), "Wrong content in Negative Values Matrix Column Major Content");    
        }
    }
    
    @Nested
    @DisplayName("Matrix Get Tests")
    class MatrixGetTests {
        //test normal matrix
        @Test public void testNormalMatrixGet() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            
            SharedMatrix matrix = new SharedMatrix(table);
            
            assertEquals(1.0, matrix.get(0).get(0), "Matrix get");
        }
        //test null matrix
        @Test public void testNullMatrixGet() {
            SharedMatrix nullMatrix = null;
            assertThrows(NullPointerException.class, () -> {
                nullMatrix.get(0);
            }, "Expected get() to throw, but it didn't");
        }
        //test empty matrix
        @Test public void testEmptyMatrixGet() {
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertThrows(IndexOutOfBoundsException.class, () -> {
                emptyMatrix.get(0);
            }, "Expected get() to throw, but it didn't");
        }
        //test out of bounds index
        @Test public void testOutOfBoundsMatrixGet() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertThrows(IndexOutOfBoundsException.class, () -> {
                matrix.get(2);
            }, "Expected get() to throw, but it didn't");
        }
        //test negative index
        @Test public void testNegativeIndexMatrixGet() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertThrows(IndexOutOfBoundsException.class, () -> {
                matrix.get(-1);
            }, "Expected get() to throw, but it didn't");
        }
        //test single row matrix
        @Test public void testSingleRowMatrixGet() {
            double[][] table = {
                {7.0, 8.0, 9.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertEquals(9.0, matrix.get(0).get(2), "Single Row Matrix Get");
        }
        //test single column matrix
        @Test public void testSingleColumnMatrixGet() {
            double[][] table = {
                {10.0},
                {11.0},
                {12.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertEquals(12.0, matrix.get(2).get(0), "Single Column Matrix Get");
        }
        //test large matrix
        @Test public void testLargeMatrixGet() {
            int rows = 50;
            int cols = 50;
            double[][] largeTable = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    largeTable[i][j] = i * cols + j;
                }
            }
            SharedMatrix largeMatrix = new SharedMatrix(largeTable);
            assertEquals(2499.0, largeMatrix.get(49).get(49), "Large Matrix Get");
        }
        //test matrix with negative values
        @Test public void testNegativeValuesMatrixGet() {
            double[][] negTable = {
                {-1.0, -2.0, -3.0},
                {-4.0, -5.0, -6.0},
            };
            SharedMatrix negMatrix = new SharedMatrix(negTable);
            assertEquals(-5.0, negMatrix.get(1).get(1), "Negative Values Matrix Get");
        }
    }

    @Nested
    @DisplayName("Matrix Length Tests")
    class MatrixLengthTests {
        //test normal matrix
        @Test public void testNormalMatrixLength() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertEquals(2, matrix.length(), "Matrix Length did not match expected output");
        }
        //test null matrix
        @Test public void testNullMatrixLength() {
            SharedMatrix nullMatrix = null;
            assertThrows(NullPointerException.class, () -> {
                nullMatrix.length();
            }, "Expected length() to throw, but it didn't");
        }
        //test empty matrix
        @Test public void testEmptyMatrixLength() {
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertEquals(0, emptyMatrix.length(), "Empty Matrix Length did not match expected output");
        }
        //test single row matrix
        @Test public void testSingleRowMatrixLength() {
            double[][] table = {
                {7.0, 8.0, 9.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertEquals(1, matrix.length(), "Single Row Matrix Length did not match expected output");
        }
        //test single column matrix
        @Test public void testSingleColumnMatrixLength() {
            double[][] table = {
                {10.0},
                {11.0},
                {12.0},
            };
            SharedMatrix matrix = new SharedMatrix(table);
            assertEquals(3, matrix.length(), "Single Column Matrix Length did not match expected output");
        }
    }

    @Nested 
    @DisplayName("Matrix Orientation Tests")
    class MatrixOrientationTests {
        //test row-major matrix
        @Test public void testRowMajorMatrixOrientation() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            SharedMatrix rowMatrix = new SharedMatrix();
            rowMatrix.loadRowMajor(table);
            assertEquals(VectorOrientation.ROW_MAJOR, rowMatrix.getOrientation(), "Row-Major Matrix Orientation did not match expected output");}
        //test column-major matrix
        @Test public void testColumnMajorMatrixOrientation() {
            double[][] table = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
            };
            SharedMatrix colMatrix = new SharedMatrix();
            colMatrix.loadColumnMajor(table);
            assertEquals(VectorOrientation.COLUMN_MAJOR, colMatrix.getOrientation(), "Column-Major Matrix Orientation did not match expected output");
        }
        //test null matrix
        @Test public void testNullMatrixOrientation() {
            SharedMatrix nullMatrix = null;
            assertThrows(NullPointerException.class, () -> {
                nullMatrix.getOrientation();
            }, "Expected getOrientation() to throw, but it didn't");
        }
        //test empty matrix
        @Test public void testEmptyMatrixOrientation() {
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertThrows(IllegalStateException.class, () -> {
                emptyMatrix.getOrientation();
            }, "Expected getOrientation() to throw, but it didn't");
        }
    }

    @Nested
    @DisplayName("Matrix Row Major Read Tests")
    class RowMajorReadTests {
        //test normal matrix
        @Test public void testNormalMatrixRowMajorRead() {
            double[][] table = { {1,2}, {3,4} };
            SharedMatrix rowTable = new SharedMatrix(); 
            SharedMatrix colTable = new SharedMatrix();
            
            rowTable.loadRowMajor(table);
            colTable.loadColumnMajor(table);
            
            double[][] readRowTable = rowTable.readRowMajor();
            double[][] readColTable = colTable.readRowMajor();

            double[][] expected = { {1,2}, {3,4} };

            assertArrayEquals(expected, readRowTable, "Row major read did not match expected output");
            assertArrayEquals(expected, readColTable, "Column major read did not match expected output");
        }
        //test single row matrix
        @Test public void testSingleRowMatrixRowMajorRead() {
            double[][] line = { {1,2,3} };

            SharedMatrix rowLine = new SharedMatrix(); 
            SharedMatrix colLine = new SharedMatrix();

            rowLine.loadRowMajor(line);
            colLine.loadColumnMajor(line);

            double[][] readRowLine = rowLine.readRowMajor();
            double[][] readColLine = colLine.readRowMajor();    
            
            assertArrayEquals(line,readRowLine,  "Row major read of single row did not match expected output");
            assertArrayEquals(line, readColLine, "Column major read of single row did not match expected output");
        }
        //test single element matrix
        @Test public void testSingleElementMatrixRowMajorRead() {
            double[][] single = { {5.0} };

            SharedMatrix rowSingle = new SharedMatrix(); 
            SharedMatrix colSingle = new SharedMatrix();

            rowSingle.loadRowMajor(single);
            colSingle.loadColumnMajor(single);

            double[][] readRowSingle = rowSingle.readRowMajor();
            double[][] readColSingle = colSingle.readRowMajor();    

            assertArrayEquals(single, readRowSingle, "Row major read of single element did not match expected output");
            assertArrayEquals(single, readColSingle, "Column major read of single element did not match expected output");
        }
        //test empty matrix
        @Test public void testEmptyMatrixRowMajorRead() {
            SharedMatrix emptyMatrix = new SharedMatrix();
            assertThrows(IllegalStateException.class, ()-> {emptyMatrix.readRowMajor();}, "Expected readRowMajor() to throw, but it didn't");
        }
        //test null matrix
        @Test public void testNullMatrixRowMajorRead() {
            SharedMatrix nullMatrix = null;
            assertThrows(NullPointerException.class, ()-> {nullMatrix.readRowMajor();}, "Expected readRowMajor() to throw, but it didn't");
        }
    }
}

