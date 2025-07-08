import processor.LogProcessor;
import thread.Producer;
import thread.Consumer;

public class MainApp {
    public static void main(String[] args) {
        LogProcessor processor = new LogProcessor();

        Thread consumer = new Consumer(processor);
        consumer.start();

        new Producer(processor, "CRITICAL", 1).start();
        new Producer(processor, "INFO", 3).start();
        new Producer(processor, "DEBUG", 5).start();
    }
}
