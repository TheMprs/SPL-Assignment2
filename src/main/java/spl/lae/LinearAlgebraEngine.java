package spl.lae;

import java.util.List;

import memory.SharedMatrix;
import memory.VectorOrientation;
import parser.ComputationNode;
import parser.ComputationNodeType;
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

        ComputationNodeType type = node.getNodeType();
        if(type == ComputationNodeType.MATRIX){
            return;
        }
        node.associativeNesting();
        ComputationNode toResolve = node.findResolvable(); //Won't be null because already checked if it's a matrix
        List<Runnable> tasks;
        switch (toResolve.getNodeType()) {
            case ADD:
                //For addition, both are row-major
                leftMatrix.loadRowMajor(toResolve.getChildren().get(0).getMatrix());
                rightMatrix.loadRowMajor(toResolve.getChildren().get(1).getMatrix());
                tasks = createAddTasks();
                break;
            case NEGATE:
                leftMatrix.loadRowMajor(toResolve.getChildren().get(0).getMatrix());
                tasks = createNegateTasks();
                break;
            case MULTIPLY:
                //For multiply convenience, left is row-major, right is column-major
                leftMatrix.loadRowMajor(toResolve.getChildren().get(0).getMatrix());
                rightMatrix.loadColumnMajor(toResolve.getChildren().get(1).getMatrix());
                tasks = createMultiplyTasks();
                break;
            case TRANSPOSE:
                leftMatrix.loadRowMajor(toResolve.getChildren().get(0).getMatrix());
                tasks = createTransposeTasks();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported computation node type"); //Should not reach here
        }
        //executor.submitAll(tasks); //Uncomment when ready to run parallel tasks
        

    }

    
    public List<Runnable> createAddTasks() {
        // Done: return tasks that perform row-wise addition
        if(leftMatrix == null || rightMatrix == null){
            throw new NullPointerException("Matrices not loaded");
        }
         //Check dimensions to ensure they can be added
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
        // Done: return tasks that perform row × matrix multiplication

        //compare left's cols to right's rows
        if(leftMatrix.get(0).length() != rightMatrix.get(0).length()){
            throw new IllegalArgumentException("Matrices dimensions do not match for multiplication");
        }
        
        //verify orientations
        if(leftMatrix.getOrientation() != VectorOrientation.ROW_MAJOR ||
            rightMatrix.getOrientation() != VectorOrientation.COLUMN_MAJOR){
            throw new UnsupportedOperationException("Matrices multiplication with same orientations not supported");
        }

        List<Runnable> tasks = new java.util.LinkedList<Runnable>();
        for (int i = 0; i < leftMatrix.length(); i++) {
            final int index = i; //Capture index for lambda scope
            Runnable multCurRow = () -> {
                    leftMatrix.get(index).vecMatMul(rightMatrix);
            };
            tasks.add(multCurRow);
        }
        return tasks;
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
        // Done: return tasks that transpose rows
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
