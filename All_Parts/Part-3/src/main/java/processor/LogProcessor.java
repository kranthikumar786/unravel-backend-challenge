package processor;

import comparator.LogTaskComparator;
import model.LogTask;

import java.util.concurrent.PriorityBlockingQueue;

public class LogProcessor {

    private final PriorityBlockingQueue<LogTask> queue;

    public LogProcessor() {
        this.queue = new PriorityBlockingQueue<>(100, LogTaskComparator.BY_PRIORITY_THEN_TIMESTAMP);
    }

    public void submit(LogTask task) {
        queue.offer(task);
    }

    public LogTask consume() throws InterruptedException {
        return queue.take();
    }
}
