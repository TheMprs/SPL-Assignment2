package scheduling;

import java.util.LinkedList;
import java.util.List;

//Don't forget to remove this class before submission to moodle
public class SequentialExecutor {
    public SequentialExecutor(int numThreads) {
        if (numThreads != 1){
            throw new IllegalArgumentException("SequentialExecutor only supports 1 thread");
        }
    }
    public void submit(Runnable task) {
        task.run();
    }

    public void submitAll(Iterable<Runnable> tasks) {
        // Done: submit tasks one by one and wait until all finish
        for(Runnable task : tasks){
            task.run();
        }
    }

    public void shutdown() throws InterruptedException {
        return;
    }
}
