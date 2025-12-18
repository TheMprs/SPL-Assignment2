import memory.SharedMatrix;

public class TestSharedMatrix {
    public static void main(String[] args) {
        TestMatrixToString();
    }

    public static void TestMatrixToString(){
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        SharedMatrix matrix = new SharedMatrix(matrixData);
        System.out.println(matrix.toString());

        SharedMatrix matrix2 = new SharedMatrix();
        matrix2.loadColumnMajor(matrixData);

        System.out.println(matrix2.toString());
    }
}
