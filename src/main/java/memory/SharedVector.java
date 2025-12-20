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
        readLock();
        try{
            if (index < 0 || index >= vector.length){
            throw new IndexOutOfBoundsException("Iligal index");
            }
            return vector[index];
        } finally {
            // Ensures that the lock is released even if an exception occurs
            readUnlock();
        }
    }

    public int length() {
        // Done: return vector length
        readLock();
        try{
            return vector.length;
        } finally {
            //Unlocks after the return value is stored, right before exiting the method
            readUnlock();
        }        
    }

    public VectorOrientation getOrientation() {
        // Done: return vector orientation
        readLock();
        try{
            return orientation;
        } finally {
            //Unlocks after the return value is stored, right before exiting the method
            readUnlock();
        }   
    }

    public void writeLock() {
        // Done: acquire write lock
        lock.writeLock().lock();
    }

    public void writeUnlock() {
        // Done: release write lock
        lock.writeLock().unlock();
    }

    public void readLock() {
        // Done: acquire read lock
        lock.readLock().lock();
    }

    public void readUnlock() {
        // Done: release read lock
        lock.readLock().unlock();
    }

    public void transpose() {
        // Done: transpose vector
        
        writeLock();
        orientation = orientation == VectorOrientation.ROW_MAJOR ? VectorOrientation.COLUMN_MAJOR 
                                        : VectorOrientation.ROW_MAJOR;
        writeUnlock();
    }

    public void add(SharedVector other) {
        // Done: add two vectors
        try{
            writeLock();
            if(other == null){throw new NullPointerException("Other vector is null");}
            if(vector.length != other.length()){throw new IllegalArgumentException("other vector has different length from this vector");}

            //Summing the result into this
            for (int i = 0; i < vector.length; i++) {
                // No deadlock risk: In the engine we always write to the Left Matrix and read from the Right Matrix.
                vector[i] = vector[i]+ other.get(i);
            }
        }
        finally{
            writeUnlock();
        }
        
    }

    public void negate() {
        // Done: negate vector
        try{
            writeLock();
            for (int i = 0; i < vector.length; i++) {
            vector[i] = -vector[i];
        }
        } finally {
            writeUnlock();
        }
    }

    public double dot(SharedVector other) {
        // Done: compute dot product (row · column)
        try{
            readLock();
            if(other == null){throw new NullPointerException("Other vector is null");}
            if(vector.length != other.length()){throw new IllegalArgumentException("other vector has different length from this vector");}
            double sum = 0;
            for (int i = 0; i < vector.length; i++) {
                // Using get() per iteration incurs overhead but prevents deadlocks
                // by avoiding nested locks without global ordering.
                sum += vector[i]*other.get(i);
            }
            return sum;
        }
        finally{
            readUnlock();
        }
    }

    public void vecMatMul(SharedMatrix matrix) {
        //Done: compute row-vector × matrix 
        try{
            writeLock();
            if(this.orientation != VectorOrientation.ROW_MAJOR){
            throw new UnsupportedOperationException("vecMatMul not supported for non-row major vectors");
            }
            if(matrix.getOrientation() != VectorOrientation.COLUMN_MAJOR){
                throw new UnsupportedOperationException("vecMatMul not supported for non-column major matrices");
            }
            double[] result = new double[matrix.length()];
            for (int i = 0; i < result.length; i++) {
                result[i] = dot(matrix.get(i));
            }
            this.vector = result;
        }
        finally{
            writeUnlock();
        }
    }

    @Override
    public boolean equals(Object other) {
        
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;
        
        SharedVector otherVector = (SharedVector) other;
        readLock();
        try{
            if (orientation != otherVector.getOrientation() || length() != otherVector.length()){return false;}
        for (int i = 0; i < vector.length; i++) {
            if(vector[i]!= otherVector.get(i)){
                return false;
            }
        }
        return true;
        } finally {
            readUnlock();
        }
        
    }
    
    @Override 
    public String toString(){
        readLock();
        try{
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
            } else { // column major
                sb.append("|");
                for (int i = 0; i < vector.length; i++) {
                    sb.append(" ").append(vector[i]).append(" |");
                }
            }
            return sb.toString();
        }
        finally{
            readUnlock();
        }
    }
}
