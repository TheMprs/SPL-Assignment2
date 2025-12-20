package scheduling;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TiredThread extends Thread implements Comparable<TiredThread> {

    private static final Runnable POISON_PILL = () -> {}; // Special task to signal shutdown

    private final int id; // Worker index assigned by the executor
    private final double fatigueFactor; // Multiplier for fatigue calculation

    private final AtomicBoolean alive = new AtomicBoolean(true); // Indicates if the worker should keep running

    // Single-slot handoff queue; executor will put tasks here
    private final BlockingQueue<Runnable> handoff = new ArrayBlockingQueue<>(1);

    private final AtomicBoolean busy = new AtomicBoolean(false); // Indicates if the worker is currently executing a task

    private final AtomicLong timeUsed = new AtomicLong(0); // Total time spent executing tasks
    private final AtomicLong timeIdle = new AtomicLong(0); // Total time spent idle
    private final AtomicLong idleStartTime = new AtomicLong(0); // Timestamp when the worker became idle

    public TiredThread(int id, double fatigueFactor) {
        this.id = id;
        this.fatigueFactor = fatigueFactor;
        this.idleStartTime.set(System.nanoTime());
        setName(String.format("FF=%.2f", fatigueFactor));
    }

    public int getWorkerId() {
        return id;
    }

    public double getFatigue() {
        return fatigueFactor * timeUsed.get();
    }

    public boolean isBusy() {
        return busy.get();
    }

    public long getTimeUsed() {
        return timeUsed.get();
    }

    public long getTimeIdle() {
        return timeIdle.get();
    }

    /**
     * Assign a task to this worker.
     * This method is non-blocking: if the worker is not ready to accept a task,
     * it throws IllegalStateException.
     */
    public void newTask(Runnable task) {
        // TODO
        // the add operation is non-blocking, will throw exception if full
        handoff.add(task);
    }

    /**
     * Request this worker to stop after finishing current task.
     * Inserts a poison pill so the worker wakes up and exits.
     */
    public void shutdown() {
        // Done: insert poison pill into handoff queue
        // the put operation is blocking, meaning it will wait until there is space
        // in the queue to insert the poison pill
        try {
            handoff.put(POISON_PILL);
        } catch (InterruptedException e) { 
            // if interrupted while waiting we just restore the interrupt status
            // interrupted means someone wants to stop the thread
            Thread.currentThread().interrupt(); 
        }
    }

    @Override
    public void run() {
        // Done
        // check if there are tasks to perform, and not busy
        // get task from handoff, run it, update timeUsed and timeIdle
        while(alive.get()){

            // thread is now idle, record start time
            idleStartTime.set(System.nanoTime());
                        
            try { 
                // we use try because take() can throw InterruptedException
                // if interrupted while waiting
                // take() is blocking, waits until a task is available
                Runnable task = handoff.take();
                
                long jobStartTime = System.nanoTime();
                // time idle is (totalTimeIdle + (current time - time spent waiting for current job))
                timeIdle.addAndGet(System.nanoTime() - idleStartTime.get());
                
                // check for poison pill
                if(task == POISON_PILL){
                    alive.set(false);
                }
                else { 
                    // start executing task
                    busy.set(true);
                    
                    try {
                        task.run();
                    }
                    catch (Exception e) {
                        // math failed, log and continue
                        System.out.println("worker " + id + " encountered an exception while executing a task: " + e.getMessage());
                    }
                    finally {
                        // time used is (totalTimeUsed + (current time - job start time))
                        timeUsed.addAndGet(System.nanoTime() - jobStartTime);
                        // time used is (current time - job time start + previous time used)
                        busy.set(false);        
                    }
                }
            }
            catch (InterruptedException e) {
                // executor called for shutdown while waiting for a task
                Thread.currentThread().interrupt();
                alive.set(false);    
            } 
        }
    }

    @Override
    public int compareTo(TiredThread o) {
        // TODO
        // compare fatigues between threads, 
        // making the most tired one have the lowest priority
        if(o.getFatigue() < this.getFatigue()) return 1;
        else if(o.getFatigue() > this.getFatigue()) return -1;
        return 0;
    }
}