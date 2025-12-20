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
        // TODO
        alive.set(false);
        handoff.offer(POISON_PILL);
        //update worker values;
    }

    @Override
    public void run() {
        // TODO
        // check if there are tasks to perform, if alive, and not busy
        // get task from handoff, run it, update timeUsed and timeIdle
        while(!handoff.isEmpty() && !isBusy() && isAlive()){
            Runnable task = handoff.remove();
            // check for poison pill
            if(task == POISON_PILL){
                shutdown();
            }
            else { 
                // update thread stats
                busy.set(true);
                idleStartTime.set(System.nanoTime());
                
                task.run();
                // update time stats
                timeIdle.addAndGet(System.nanoTime() - getTimeIdle());
                timeUsed.addAndGet(System.nanoTime() - idleStartTime.get());
                busy.set(false);
            }
        }
    }

    @Override
    public int compareTo(TiredThread o) {
        // TODO
        // compare fatigues between threads, 
        // making the highest fatigued thread be the bigger one
        if(o.getFatigue() < this.getFatigue()) return -1;
        else if(o.getFatigue() > this.getFatigue()) return 1;
        return 0;
    }
}