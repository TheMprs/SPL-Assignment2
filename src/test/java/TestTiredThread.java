import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import scheduling.TiredThread;
public class TestTiredThread {
    @Test
    @DisplayName("Positive Test: Compare workers by fatigue")
    public void testCompareToFatigue() {
        
        TiredThread freshWorker = new TiredThread(1, 1.0); // עייפות התחלתית 0
        TiredThread tiredWorker = new TiredThread(2, 2.0); // עייפות התחלתית 0

        //Fatigue:
        // freshWorker: 1.0 * 100 = 100.0 
        // tiredWorker: 2.0 * 100 = 200.0
        freshWorker.addTimeUsed(100);
        tiredWorker.addTimeUsed(100);

        // Check comparisons:

        // Fresh worker "less than" tired worker (higher priority)
        assertTrue(freshWorker.compareTo(tiredWorker) < 0, 
            "Fresher worker should be 'less than' tired worker (higher priority).");

        // Tired should be "greater than" fresh worker (lower priority in min-heap)
        assertTrue(tiredWorker.compareTo(freshWorker) > 0, 
            "Tired worker should be 'greater than' fresh worker (lower priority).");

        // Comparing is reflexive :)
        assertEquals(0, freshWorker.compareTo(freshWorker), 
            "Comparing a worker to itself should return 0.");
    }
    
    @Test
    @DisplayName("Positive Test: Poison Pill terminates the thread")
    public void testPoisonPillShutdown() throws InterruptedException {
        TiredThread worker = new TiredThread(1, 1.0);
        worker.start(); 

        assertTrue(worker.isAlive(), "Worker should be alive after start()");

       //Put the poison pill in the worker's queue
        worker.shutdown();

        //We let the worker some time to process the poison pill and terminate
        worker.join(1000); 

        //By now, the worker should have terminated
        assertFalse(worker.isAlive(), "Worker should have terminated after receiving the Poison Pill");
    }
    
    @Test
    @DisplayName("Positive Test: Precise fatigue calculation")
    public void testFatigueCalculationAccuracy() {
        double factor = 1.5;
        TiredThread worker = new TiredThread(1, factor);
        
        assertEquals(0.0, worker.getFatigue(), "Initial fatigue must be 0.0");

        long timeToAdd = 1000;
        worker.addTimeUsed(timeToAdd);
        double expectedFatigue = factor * timeToAdd;
        
        //Delta is because of floating point precision
        assertEquals(expectedFatigue, worker.getFatigue(), 0.0001, 
            "Fatigue calculation should strictly follow: factor * timeUsed");

        //Try another addition
        worker.addTimeUsed(500);
        double secondExpected = factor * (1000 + 500);
        assertEquals(secondExpected, worker.getFatigue(), 0.0001, 
            "Fatigue must update cumulatively after multiple tasks");
    }

    @Test
    @DisplayName("Negative Test: newTask throws exception after shutdown")
    void testNewTaskThrowsAfterShutdown() {
        TiredThread t = new TiredThread(1, 1.0);
        
        t.shutdown(); 
        
        assertThrows(IllegalStateException.class, () -> {
            t.newTask(() -> System.out.println("Should not run"));
        }, "A worker must not accept new tasks after shutdown has been initiated.");
    }

    @Test
    @DisplayName("Negative Test: newTask throws when handoff queue is full")
    void testNewTaskThrowsWhenFull() {
        //We don't start the thread so its queue will stay full after one task is added
        TiredThread t = new TiredThread(1, 1.0);
        t.newTask(() -> {}); 

        //Now the queue is full, next newTask should throw exception
        assertThrows(IllegalStateException.class, () -> {
            t.newTask(() -> {});
        }, "Worker should reject new tasks if its handoff queue is already full");
    }
    
    @Test
    @DisplayName("Positive Test: Idle time increases while waiting for tasks")
    void testIdleTimeAccounting() throws InterruptedException {
        TiredThread t = new TiredThread(1, 1.0);
        t.start();
        
        //Give it a moment to ensure the thread is running
        Thread.sleep(50); 

        //Record initial idle time
        long initialIdle = t.getTimeIdle();
        
        // Activity pause to let idle time accumulate
        long sleepTime = 100;
        Thread.sleep(sleepTime);

        // Wake up the thread with a task so it can update its idle time
        t.newTask(() -> {});
        
        //Let the thread process the task
        Thread.sleep(20);
        long finalIdle = t.getTimeIdle();
        
        assertTrue(finalIdle > initialIdle, 
            "Idle time should increase after a period of inactivity");
            
        // Making sure idle time makes sense with respect to sleep duration
        assertTrue(finalIdle - initialIdle >= TimeUnit.MILLISECONDS.toNanos(sleepTime),
            "Idle time growth should match the actual elapsed time");

        t.shutdown();
        //// Wait up to 500 milliseconds for the
        //worker thread to terminate to ensure a clean test environment.
        t.join(1000);
    }

    @Test
    @DisplayName("Negative Test: newTask throws IllegalArgumentException for null task")
    void testNewTaskNullThrows() throws InterruptedException {
        TiredThread t = new TiredThread(0, 1.0);
        t.start();
        
        try {
                assertThrows(IllegalArgumentException.class, () -> {
                t.newTask(null);
            }, "The worker must reject null tasks to prevent thread death.");
            
        } finally {
            // 3. Cleanup
            t.shutdown();
            t.join(500);
        }
    }
}
