package spl.lae;

import java.util.List;

import memory.SharedMatrix;
import parser.ComputationNode;
import scheduling.TiredExecutor;

public class LinearAlgebraEngine {

    private SharedMatrix leftMatrix = new SharedMatrix();
    private SharedMatrix rightMatrix = new SharedMatrix();
    private TiredExecutor executor;

    public LinearAlgebraEngine(int numThreads) {
        // Done: create executor with given thread count
        executor = new TiredExecutor(numThreads);
    }

    public ComputationNode run(ComputationNode computationRoot) {
        // TODO: resolve computation tree step by step until final matrix is produced
        return null;
    }

    public void loadAndCompute(ComputationNode node) {
        // TODO: load operand matrices
        // TODO: create compute tasks & submit tasks to executor
    }

    //Currently only supports same orientation addition. Still need to handle row-major + col-major
    public List<Runnable> createAddTasks() {
        // TODO: return tasks that perform row-wise addition
        if(leftMatrix == null || rightMatrix == null){
            throw new NullPointerException("Matrices not loaded");
        }
        if(leftMatrix.length() != rightMatrix.length() || 
            leftMatrix.get(0).length() != rightMatrix.get(0).length()){
            throw new IllegalArgumentException("Matrices dimensions do not match for addition");
        }
        List<Runnable> tasks = new java.util.LinkedList<Runnable>();
        for (int i = 0; i < leftMatrix.length(); i++) {
            final int index = i; //Capture index for lambda scope
            Runnable addCurRow = () -> {
                    leftMatrix.get(index).add(rightMatrix.get(index));
            };
            tasks.add(addCurRow);
        }
        return tasks;
    }

    public List<Runnable> createMultiplyTasks() {
        // TODO: return tasks that perform row × matrix multiplication
        return null;
    }

    public List<Runnable> createNegateTasks() {
        // Done: return tasks that negate rows
        List<Runnable> tasks = new java.util.LinkedList<Runnable>();
        for (int i = 0; i < leftMatrix.length(); i++) {
            final int index = i; //Capture index for lambda scope
            Runnable negateCurRow = () -> {
                    leftMatrix.get(index).negate();
            };
            tasks.add(negateCurRow);
        }
        return tasks;
    }

    public List<Runnable> createTransposeTasks() {
        // TODO: return tasks that transpose rows
        List<Runnable> tasks = new java.util.LinkedList<Runnable>();
        for (int i = 0; i < leftMatrix.length(); i++) {
            final int index = i; //Capture index for lambda scope
            Runnable transposeCurRow = () -> {
                    leftMatrix.get(index).transpose();
            };
            tasks.add(transposeCurRow);
        }
        return tasks;
    }

    public String getWorkerReport() {
        // TODO: return summary of worker activity
        return null;
    }
}
