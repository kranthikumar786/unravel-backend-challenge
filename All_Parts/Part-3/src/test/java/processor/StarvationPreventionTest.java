package processor;

import model.LogTask;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class StarvationPreventionTest {

    @Test
    void testHighPriorityTaskIsNotStarved() throws InterruptedException {
        LogProcessor processor = new LogProcessor();

        // Flood with low-priority logs (priority 5)
        for (int i = 0; i < 1000; i++) {
            processor.submit(new LogTask("Low-priority log " + i, "DEBUG", 5));
        }

        // Slight delay to simulate late high-priority arrival
        Thread.sleep(50);

        // High-priority log (priority 1)
        LogTask criticalLog = new LogTask("CRITICAL log", "CRITICAL", 1);
        processor.submit(criticalLog);

        // Consume one task (should be high-priority, not flooded ones)
        LogTask consumed = processor.consume();

        assertEquals("CRITICAL log", consumed.getMessage());
        assertEquals(1, consumed.getPriority());
    }
}
