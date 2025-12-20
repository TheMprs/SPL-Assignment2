package scheduling;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TiredExecutor {

    private final TiredThread[] workers;
    private final PriorityBlockingQueue<TiredThread> idleMinHeap = new PriorityBlockingQueue<>();
    private final AtomicInteger inFlight = new AtomicInteger(0);

    public TiredExecutor(int numThreads) {
        // TODO
        workers = new TiredThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = new TiredThread(i, 0.5+ Math.random());
            workers[i].start();
            idleMinHeap.add(workers[i]);
        }
       
    }

        public void submit(Runnable task) {
            // TODO
            try{
                inFlight.incrementAndGet();
                TiredThread curWorker = idleMinHeap.take();
                // Wrap the task to update worker status and inFlight counter
                Runnable wrappedTask = () -> {
                    try{
                        task.run(); // Execute the original task
                    }
                    finally{
                        idleMinHeap.add(curWorker); // Mark worker as idle again
                        if(inFlight.decrementAndGet() == 0){ //it was the last task
                        synchronized (inFlight) {
                            inFlight.notifyAll();
                        }
                    }
                    
                    }
                };
                curWorker.newTask(wrappedTask);
            }
            catch (InterruptedException e){ //The task was interrupted so we revert the inFlight increment
                inFlight.decrementAndGet();
                Thread.currentThread().interrupt();
            }
        
        }

    public void submitAll(Iterable<Runnable> tasks) {
        // TODO: submit tasks one by one and wait until all finish
        try
    }

    public void shutdown() throws InterruptedException {
        // Done
        for (TiredThread worker : workers) {
            worker.shutdown();

        }
    }

    public synchronized String getWorkerReport() {
        // TODO: return readable statistics for each worker
        String str ="";
        for (TiredThread worker : workers) {
            str += "Worker "+ worker.getId() +" Work: "+ worker.getTimeUsed() + 
            " Idle: "+ worker.getTimeIdle() + "\n";
        }
        return str;
    }
}
