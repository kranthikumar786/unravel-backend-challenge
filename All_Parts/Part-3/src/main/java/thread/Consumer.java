package thread;

import model.LogTask;
import processor.LogProcessor;

public class Consumer extends Thread {

    private final LogProcessor processor;

    public Consumer(LogProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                LogTask task = processor.consume();
                System.out.println("Consumed: " + task);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
