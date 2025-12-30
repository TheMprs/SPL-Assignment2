import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import scheduling.TiredExecutor;

/**
 * Unit Test for TiredExecutor.
 */
public class TestTiredExecutor {

    private TiredExecutor executor; //Object Under Test (OUT)
    private final int NUM_THREADS = 2;

    @BeforeEach
    public void setUp() {
        executor = new TiredExecutor(NUM_THREADS);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        // Clean up resources after each test
        executor.shutdown();
    }

    @Test
    @DisplayName("Positive Test: Single task execution")
    public void testSubmitTask() {
        // Create a mock Runnable task and add it to a new task list
        MockRunnable task = new MockRunnable();
        java.util.List<Runnable> taskList = java.util.Collections.singletonList(task);

        //We use submitAll as opposed to sumbit, to ensure
        //the task is completed before we assert.
        executor.submitAll(taskList);

        assertTrue(task.wasRun, "The submitted task should have been executed by a worker thread.");
    }

    @Test
    @DisplayName("Positive Test: Task execution affects worker report")
    public void testReportAfterTask() throws InterruptedException {
        MockRunnable task = new MockRunnable();
        executor.submitAll(java.util.Collections.singletonList(task));
        String report = executor.getWorkerReport();
        //Ensure report format
        assertNotNull(report, "Report should not be null");
        assertTrue(report.contains("Work:"), "Report should contain work time");
        assertTrue(report.contains("Fairness Factor:"), "Report should include fairness calculation");

        //Ensure the work time has increased after task execution
        String workPart = report.split("Work: ")[1].split(" ")[0];
        long workTime = Long.parseLong(workPart);
        assertTrue(workTime > 0, "Work time should be greater than 0 after task execution.");
        
        //Ensure fatigue has been updated
        String fatiguePart = report.split("Fatigue: ")[1].split("\n")[0];
        double fatigue = Double.parseDouble(fatiguePart);
        assertTrue(fatigue > 0.0, "Fatigue should be updated and greater than 0.");

        //Ensure fairness factor is calculated
        String fairnessPart = report.split("Fairness Factor: ")[1].trim();
        double fairness = Double.parseDouble(fairnessPart);
        assertTrue(fairness > 0.0, "Fairness Factor should be positive after work execution");
    }
    
    @Test
    @DisplayName("Positive Test: Executor selects the least tired worker")
    public void testWorkerSelectionByFatigue() throws InterruptedException {        
        MockRunnable task1 = new MockRunnable();
        MockRunnable task2 = new MockRunnable();

        //We use submitAll to ensure each task is completed before proceeding
        executor.submitAll(java.util.Collections.singletonList(task1));

        //By now, the first worker should have some fatigue,
        // so the next task should go to the other worker
        executor.submitAll(java.util.Collections.singletonList(task2));
        String report = executor.getWorkerReport();
        
        // We check that both workers have done some work
        int workersWithWork = 0;
        String[] lines = report.split("\n");
        for (String line : lines) {
            if (line.contains("Work:") && !line.contains("Work: 0")) {
                workersWithWork++;
            }
        }

        assertEquals(2, workersWithWork, "Each task should have been handled by a different worker to balance fatigue.");
    }
    
    @Test
    @DisplayName("Positive Test: Shutdown stops all worker threads")
    public void testExecutorShutdown() throws Exception {
        //Using reflection (allowed on forum discussion) to access private worker threads
        java.lang.reflect.Field field = executor.getClass().getDeclaredField("workers");
        field.setAccessible(true);
        Thread[] workers = (Thread[]) field.get(executor);

        executor.shutdown();

        //Make sure all worker threads have stopped
        for (Thread worker : workers) {
            //Each worker should terminate within 1 second
            worker.join(1000); 
            assertFalse(worker.isAlive(), "Worker thread should not be alive after shutdown.");
        }
    }
    
    @Test
    @DisplayName("Positive Test: submitAll blocks until all tasks are complete")
    public void testSubmitAllIsBlocking() throws InterruptedException {
        
        int numTasks = 5;
        java.util.List<Runnable> tasks = new java.util.ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            tasks.add(new MockRunnable()); //Each task takes time
        }

        // 2. Act: קריאה ל-submitAll
        // השרד של הטסט אמור "להיתקע" כאן עד שכולן יסתיימו
        executor.submitAll(tasks);

        //If submitAll would not be blocking, we would reach this point before tasks complete
        for (int i = 0; i < numTasks; i++) {
            assertTrue(((MockRunnable)tasks.get(i)).wasRun, 
                "Task " + i + " should be completed BEFORE submitAll returns");
        }
    }
    
    //"When the waves get strong, the strong ones get waved..."
    @Test
    @DisplayName("CRITICAL: Stress Test - 100 tasks concurrency")
    public void testInFlightAfterStress() throws InterruptedException {
        int numTasks = 100;
        java.util.List<Runnable> tasks = new java.util.ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            tasks.add(new MockRunnable());
        }

        executor.submitAll(tasks);
        
        // Ensure all tasks were executed by the end of submitAll
        for (Runnable task : tasks) {
            assertTrue(((MockRunnable)task).wasRun, "Task lost during stress");
        }

        //We need try-catch because we are accessing a private field
        try {
            java.lang.reflect.Field field = executor.getClass().getDeclaredField("inFlight");
            field.setAccessible(true);
            java.util.concurrent.atomic.AtomicInteger inFlight = (java.util.concurrent.atomic.AtomicInteger) field.get(executor);
            
            assertEquals(0, inFlight.get(), "In-flight counter leaked!");
        } catch (Exception e) {
            fail("Test failed due to reflection error: " + e.getMessage());
        }
    }
    /**
     * Mockup implementation for Runnable.
     * Used to verify task execution.
     */
    private static class MockRunnable implements Runnable {
    public boolean wasRun = false;

    @Override
    public void run() {
        //Give the task some בשר 
        long start = System.nanoTime();
        while (System.nanoTime() - start < 1_000_000) { 
            //We actively wait for 1 millisecond
            //It's more deterministic this way than sleeping
        }
        wasRun = true;
    }
}
}
