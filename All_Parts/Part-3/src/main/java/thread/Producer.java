package thread;

import model.LogTask;
import processor.LogProcessor;

public class Producer extends Thread {

    private final LogProcessor processor;
    private final String type;
    private final int priority;

    public Producer(LogProcessor processor, String type, int priority) {
        this.processor = processor;
        this.type = type;
        this.priority = priority;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            LogTask task = new LogTask("Message " + i + " from " + type, type, priority);
            processor.submit(task);
            try {
                Thread.sleep(50); // simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
