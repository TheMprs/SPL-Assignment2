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

    //helper method to check if this==other
    @Override public boolean equals(Object other){
        if(this == other) return true;
        else { 
            if(other == null || !(this.getClass() == other.getClass()) ) 
                return false;
            SharedMatrix otherMatrix = (SharedMatrix) other;
            for (int index = 0; index < otherMatrix.length(); index++) {
                if(!this.get(index).equals(otherMatrix.get(index)))
                    return false;
            }
            return true;
        }
    }

    /**helper method to print matrix
    * vector output will change based on its orientation
    * {1,2,3} -> |1||4|  / |1 2 3|
    * {4,5,6}    |2||5|    |4 5 6|
    *            |3||6|
    * 
    */
   @Override
    public String toString(){
        String str="";
        // if(this.getOrientation() == VectorOrientation.ROW_MAJOR){
            for (int row = 0; row < vectors.length; row++) 
                str+=vectors[row].toString()+"\n";
        // }
        // else {
        //     for (int row = 0; row < vectors.length; row++) {
        //         for (int col = 0; col < vectors[0].length(); col++) {
        //             str+="|"+this.get(col).get(row)+"|";
        //         }
        //         str+="\n";
        //     }
        // }
        return str;
    }

    public void loadRowMajor(double[][] matrix) {
    // Done: replace internal data with new row-major matrix
        if(matrix==null || matrix.length==0 || matrix[0].length==0) 
            throw new IllegalArgumentException("Input matrix cannot be null or empty");
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
            double[] colData = new double[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                colData[row] = matrix[row][col];
            }
            vectors[col] = new SharedVector(colData, VectorOrientation.COLUMN_MAJOR);
        }
    }

    public double[][] readRowMajor() {
        // Done: return matrix contents as a row-major double[][]
        double[][] matrixContents;
        
        //check orientation
        if(getOrientation() == VectorOrientation.ROW_MAJOR){ //row major
            matrixContents = new double[length()][vectors[0].length()];
            for(int row=0;row< length();row++){
                for(int col=0;col<vectors[0].length();col++){
                    matrixContents[row][col]=this.get(row).get(col);
                }
            }
        }
        else{ //column major
            matrixContents = new double[vectors[0].length()][length()];
            for(int col=0;col< length();col++){
                for(int row=0;row<vectors[0].length();row++){
                    matrixContents[row][col]=this.get(col).get(row);
                }
            }
        }
        return matrixContents;
    }

    public SharedVector get(int index) {
        // Done: return vector at index
        if(index<0 || index >= vectors.length)
            throw new IndexOutOfBoundsException("index out of bounds");
        return this.vectors[index];
    }

    public int length() {
        // Done: return number of stored vectors
        return vectors.length;
    }

    public VectorOrientation getOrientation() {
        // Done: return orientation
        if(vectors == null || vectors[0]==null)
            throw new IllegalStateException("Matrix is undefined");
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
