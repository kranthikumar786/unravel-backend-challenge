package processor;

import model.LogTask;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class LogProcessorTest {

    @Test
    void testConsumeReturnsTasksInPriorityOrder() throws InterruptedException {
        LogProcessor processor = new LogProcessor();

        LogTask task1 = new LogTask("Low priority", "DEBUG", 3);
        LogTask task2 = new LogTask("High priority", "CRITICAL", 1);
        LogTask task3 = new LogTask("Medium priority", "INFO", 2);

        processor.submit(task1);
        processor.submit(task2);
        processor.submit(task3);

        // Task with lowest priority value (i.e., highest priority) comes first
        assertEquals(task2, processor.consume());
        assertEquals(task3, processor.consume());
        assertEquals(task1, processor.consume());
    }

    @Test
    void testTasksWithSamePriorityFollowCreationOrder() throws InterruptedException {
        LogProcessor processor = new LogProcessor();

        LogTask task1 = new LogTask("Task 1", "INFO", 2);
        Thread.sleep(5); // ensure different timestamp
        LogTask task2 = new LogTask("Task 2", "INFO", 2);

        processor.submit(task1);
        processor.submit(task2);

        assertEquals(task1, processor.consume()); // task1 is older, should come first
        assertEquals(task2, processor.consume());
    }

    @Test
    void testSubmitAndConsumeSingleTask() throws InterruptedException {
        LogProcessor processor = new LogProcessor();

        LogTask task = new LogTask("Single task", "DEBUG", 5);
        processor.submit(task);

        LogTask result = processor.consume();
        assertNotNull(result);
        assertEquals(task, result);
    }
}
