package processor;

import model.LogTask;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentLogProcessingTest {
    @Test
    void testConcurrentProducersAndPriorityConsumers() throws InterruptedException, ExecutionException {
        LogProcessor processor = new LogProcessor();

        int producerCount = 3;
        int logsPerProducer = 5;
        ExecutorService producerPool = Executors.newFixedThreadPool(producerCount);
        ExecutorService consumerPool = Executors.newFixedThreadPool(2);

        List<Future<?>> producers = new ArrayList<>();
        List<LogTask> allTasks = Collections.synchronizedList(new ArrayList<>());
        List<LogTask> consumedTasks = Collections.synchronizedList(new ArrayList<>());

        // Start Producers
        for (int i = 0; i < producerCount; i++) {
            final int id = i;
            producers.add(producerPool.submit(() -> {
                for (int j = 0; j < logsPerProducer; j++) {
                    int priority = new Random().nextInt(5); // Priority 0-4
                    LogTask task = new LogTask("Log " + id + "-" + j, "TYPE" + id, priority);
                    allTasks.add(task);
                    processor.submit(task);
                }
            }));
        }

        for (Future<?> f : producers) f.get();
        producerPool.shutdown();

        CountDownLatch latch = new CountDownLatch(producerCount * logsPerProducer);
        for (int i = 0; i < 2; i++) {
            consumerPool.submit(() -> {
                while (latch.getCount() > 0) {
                    try {
                        LogTask task = processor.consume();
                        consumedTasks.add(task);
                        latch.countDown();
                    } catch (InterruptedException ignored) {}
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        consumerPool.shutdownNow();

        //  Check that all logs are consumed
        assertEquals(allTasks.size(), consumedTasks.size(), "All tasks should be consumed");

        //  Check priority ordering
        for (int i = 1; i < consumedTasks.size(); i++) {
            LogTask prev = consumedTasks.get(i - 1);
            LogTask curr = consumedTasks.get(i);
            int priorityCompare = Integer.compare(prev.getPriority(), curr.getPriority());
            if (priorityCompare > 0) {
                fail("Task with lower priority appeared before a higher one");
            } else if (priorityCompare == 0) {
                // Same priority – ensure ordering by timestamp
                assertTrue(!prev.getCreatedAt().isAfter(curr.getCreatedAt()),
                        "Same-priority tasks should be ordered by timestamp");
            }
        }
    }

}
