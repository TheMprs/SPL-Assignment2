package memory;

public class SharedMatrix {

    private volatile SharedVector[] vectors = {}; // underlying vectors

    public SharedMatrix() {
        // Done: initialize empty matrix
        vectors = new SharedVector[0];
    }

    public SharedMatrix(double[][] matrix) {
        // Done: construct matrix as row-major SharedVectors
        loadRowMajor(matrix);
    }

    public void loadRowMajor(double[][] matrix) {
    // Done: replace internal data with new row-major matrix
        if(matrix==null) throw new IllegalArgumentException("Input matrix cannot be null");
        int rows = matrix.length;
        vectors = new SharedVector[rows];
        for(int i=0; i<rows;i++){
            SharedVector vector = new SharedVector(matrix[i], VectorOrientation.ROW_MAJOR);
            vectors[i]=vector;
        }
    }

    public void loadColumnMajor(double[][] matrix) {
        // Done: replace internal data with new column-major matrix
        if(matrix==null) throw new IllegalArgumentException("Input matrix cannot be null");
        int columns = matrix[0].length;
        vectors = new SharedVector[columns];
        
        for (int col = 0; col < columns; col++) {
            double[] singleColData = new double[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                singleColData[row] = matrix[row][col];
            }
            vectors[col] = new SharedVector(singleColData, VectorOrientation.COLUMN_MAJOR);
        }
    }

    public double[][] readRowMajor() {
        // Done: return matrix contents as a row-major double[][]
        double[][] matrixContents = new double[length()][vectors[0].length()];
        for(int row=0;row< length();row++){
            for(int column=0;column<vectors[0].length();column++){
                matrixContents[row][column]=this.get(row).get(column);
            }
        }
        return matrixContents;
    }

    public SharedVector get(int index) {
        // Done: return vector at index
        if(index<0 || index>vectors.length)
            throw new IndexOutOfBoundsException("index out of bounds");
        return this.vectors[index];
    }

    public int length() {
        // Done: return number of stored vectors
        return vectors.length;
    }

    public VectorOrientation getOrientation() {
        // Done: return orientation
        return vectors[0].getOrientation();
    }

    private void acquireAllVectorReadLocks(SharedVector[] vecs) {
        // TODO: acquire read lock for each vector
    }

    private void releaseAllVectorReadLocks(SharedVector[] vecs) {
        // TODO: release read locks
    }

    private void acquireAllVectorWriteLocks(SharedVector[] vecs) {
        // TODO: acquire write lock for each vector
    }

    private void releaseAllVectorWriteLocks(SharedVector[] vecs) {
        // TODO: release write locks
    }
}
