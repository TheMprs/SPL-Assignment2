package memory;

import java.util.concurrent.locks.ReadWriteLock;

public class SharedVector {

    private double[] vector;
    private VectorOrientation orientation;
    private ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    public SharedVector(double[] vector, VectorOrientation orientation) {
        // Done: store vector data and its orientation
        if (vector == null){throw new NullPointerException("Input vector is null");}
        if (orientation == null){throw new NullPointerException("Input orientation is null");}
        
        this.orientation = orientation;
        this.vector = new double[vector.length];

        //Deep copy input vector
        for (int i = 0; i< vector.length; i++) {
            this.vector[i] = vector[i]; 
        }
    
    }

    public double get(int index) {
        // Done: return element at index (read-locked)
        if (index < 0 || index > vector.length){
            throw new IndexOutOfBoundsException("Iligal index");
        }
        return vector[index];
    }

    public int length() {
        // Done: return vector length
        return vector.length;
    }

    public VectorOrientation getOrientation() {
        // Done: return vector orientation
        return orientation;
    }

    public void writeLock() {
        // TODO: acquire write lock
    }

    public void writeUnlock() {
        // TODO: release write lock
    }

    public void readLock() {
        // TODO: acquire read lock
    }

    public void readUnlock() {
        // TODO: release read lock
    }

    public void transpose() {
        // Done: transpose vector
        orientation = orientation == VectorOrientation.ROW_MAJOR ? VectorOrientation.COLUMN_MAJOR : VectorOrientation.ROW_MAJOR;
    }

    public void add(SharedVector other) {
        // Done: add two vectors
        if(other == null){throw new NullPointerException("Other vector is null");}

        //Summing the result into this
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i]+ other.get(i);
        }
    }

    public void negate() {
        // Done: negate vector
        for (int i = 0; i < vector.length; i++) {
            vector[i] = -vector[i];
        }
    }

    //Assumes both vectors have the same orientation
    public double dot(SharedVector other) {
        // Done: compute dot product (row · column)
        if(this.length() != other.length()){throw new IllegalArgumentException("other vector has different length from this vector");}
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i]*other.get(i);
        }
        return sum;
    }

    public void vecMatMul(SharedMatrix matrix) {
        // TODO: compute row-vector × matrix
         double[] result = new double[length()];
         if(matrix.getOrientation() == VectorOrientation.COLUMN_MAJOR){
             for (int i = 0; i < length(); i++) {
                result[i] = rowxcolumnMul(matrix.get(i));
             }
            }
        else{//Matrix is oriented by rows
            //Implement
        }
    }

    //Commputes row vector * column vector multipication
    private double rowxcolumnMul (SharedVector other){
        if(orientation != VectorOrientation.COLUMN_MAJOR){throw new UnsupportedOperationException("left vector is not row vector");}
        
        //row * column is the same as dot product <row, column transposed>
        return dot(other);
    }


@Override
public boolean equals(Object other) {
    
    if (this == other) return true;
    
    if (other == null || getClass() != other.getClass()) return false;
    
    
    SharedVector otherVector = (SharedVector) other;
    if (orientation != otherVector.getOrientation() || length() != otherVector.length()){return false;}
    for (int i = 0; i < vector.length; i++) {
        if(vector[i]!= otherVector.get(i)){
            return false;
        }
    }
    return true;
    }

@Override 
public String toString(){
    StringBuilder sb = new StringBuilder();
    if (orientation == VectorOrientation.ROW_MAJOR) {
        sb.append("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) {
                sb.append(" ");
            }
        }
        sb.append("]");
    } else {
        for (int i = 0; i < vector.length; i++) {
            sb.append("| ").append(vector[i]).append(" |");
            if (i < vector.length - 1) {
                sb.append("\n");
            }
        }
    }
    return sb.toString();
}
}
