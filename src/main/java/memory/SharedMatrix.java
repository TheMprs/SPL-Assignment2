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
    @Override 
    public boolean equals(Object other){
        acquireAllVectorReadLocks(vectors);

        try{
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
        finally{
            releaseAllVectorReadLocks(vectors);
        }
    }

    //helper method to print matrix
    @Override
    public String toString() {
        try {
            acquireAllVectorReadLocks(vectors);
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vectors.length; i++) {
                // Use .append to ADD to the string, don't replace it!
                sb.append(vectors[i].toString()).append("\n"); 
            }
            return sb.toString();    
        } 
        finally {
            releaseAllVectorReadLocks(vectors);
        }
    }
    
    public void loadRowMajor(double[][] matrix) {
    // Done: replace internal data with new row-major matrix
        SharedVector[] oldVectors = vectors;
    
        acquireAllVectorWriteLocks(vectors);
        try {
            if(matrix==null || matrix.length==0 || matrix[0].length==0) 
                throw new IllegalArgumentException("Input matrix cannot be null or empty");
            int rows = matrix.length;
            SharedVector[] newVectors = new SharedVector[rows];

            for(int i=0; i<rows;i++){
                SharedVector vector = new SharedVector(matrix[i], VectorOrientation.ROW_MAJOR);
                newVectors[i]=vector;
            }
            vectors = newVectors;
        }
        finally{
            releaseAllVectorWriteLocks(oldVectors);
        }
    }

    public void loadColumnMajor(double[][] matrix) {
        // Done: replace internal data with new column-major matrix
        
        //create a local copy of old vectors to release locks later
        SharedVector[] oldVectors = vectors;

        acquireAllVectorWriteLocks(vectors);
    
        try{
            if(matrix==null) throw new IllegalArgumentException("Input matrix cannot be null");
            int columns = matrix[0].length;
            SharedVector[] newVectors = new SharedVector[columns];

            for (int col = 0; col < columns; col++) {
                double[] colData = new double[matrix.length];
                for (int row = 0; row < matrix.length; row++) {
                    colData[row] = matrix[row][col];
                }
                newVectors[col] = new SharedVector(colData, VectorOrientation.COLUMN_MAJOR);
            }
            vectors = newVectors;
        }
        finally{
            releaseAllVectorWriteLocks(oldVectors);
        }
    }

    public double[][] readRowMajor() {
        // Done: return matrix contents as a row-major double[][]
        acquireAllVectorReadLocks(vectors);
        try {
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
        //use finally to ensure locks are released even if exception occurs
        finally {
            releaseAllVectorReadLocks(vectors);
        }
    }

    public SharedVector get(int index) {
        // Done: return vector at index
        acquireAllVectorReadLocks(vectors);

        try {    
            if(index<0 || index >= vectors.length)
                throw new IndexOutOfBoundsException("index out of bounds");
            
            return this.vectors[index];
    
        } 
        finally {
            releaseAllVectorReadLocks(vectors);
        }
    }

    public int length() {
        // Done: return number of stored vectors
        acquireAllVectorReadLocks(vectors);
        
        try {
            return vectors.length;
        } 
        finally {
            releaseAllVectorReadLocks(vectors);
        }
    }

    public VectorOrientation getOrientation() {
        // Done: return orientation
        acquireAllVectorReadLocks(vectors);
        
        try {
            if(vectors == null || vectors[0]==null || vectors.length==0 )
                throw new IllegalStateException("Matrix is undefined");
            return vectors[0].getOrientation();
        } 
        finally {
            releaseAllVectorReadLocks(vectors);
        }
    }

    private void acquireAllVectorReadLocks(SharedVector[] vecs) {
        // Done: acquire read lock for each vector
        for (int i = 0; i < vecs.length; i++) {
            vecs[i].readLock();
        }
    }

    private void releaseAllVectorReadLocks(SharedVector[] vecs) {
        // Done: release read locks
        for (int i = vecs.length - 1; i >= 0; i--) {
            vecs[i].readUnlock();
        }
    }

    private void acquireAllVectorWriteLocks(SharedVector[] vecs) {
        // Done: acquire write lock for each vector
        for (int i = 0; i < vecs.length; i++) {
            vecs[i].writeLock();
        }
    }

    private void releaseAllVectorWriteLocks(SharedVector[] vecs) {
        // Done: release write locks
        for (int i = vecs.length - 1; i >= 0; i--) {
            vecs[i].writeUnlock();
        }
    }
}
