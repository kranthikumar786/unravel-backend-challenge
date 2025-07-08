package simulator;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlockFreeSimulatorTest {

    @Test
    void testMethod1AndMethod2ExecuteConcurrentlyWithoutDeadlock() throws InterruptedException {
        DeadlockFreeSimulator simulator = new DeadlockFreeSimulator();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(simulator::method1);
        Future<?> future2 = executor.submit(simulator::method2);

        executor.shutdown();
        boolean finished = executor.awaitTermination(2, TimeUnit.SECONDS);

        assertTrue(finished, "Methods should complete without deadlock.");
        assertTrue(future1.isDone(), "method1 should complete");
        assertTrue(future2.isDone(), "method2 should complete");
    }

    @Test
    void testMultipleConcurrentExecutions() throws InterruptedException {
        DeadlockFreeSimulator simulator = new DeadlockFreeSimulator();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++) {
            executor.submit(simulator::method1);
            executor.submit(simulator::method2);
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(finished, "All method executions should complete without deadlock.");
    }

    @Test
    void testMethod1RunsWithinTimeout() {
        DeadlockFreeSimulator simulator = new DeadlockFreeSimulator();
        assertTimeoutPreemptively(Duration.ofMillis(500), simulator::method1);
    }

    @Test
    void testMethod2RunsWithinTimeout() {
        DeadlockFreeSimulator simulator = new DeadlockFreeSimulator();
        assertTimeoutPreemptively(Duration.ofMillis(500), simulator::method2);
    }
}
