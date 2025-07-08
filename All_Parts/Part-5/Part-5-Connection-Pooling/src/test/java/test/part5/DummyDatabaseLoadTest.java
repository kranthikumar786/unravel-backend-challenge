package part5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DummyDatabaseLoadTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void simulateConnectionLoad() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                try (Connection conn = dataSource.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("SELECT 1");
                    ps.execute();
                    Thread.sleep(100); // simulate short query
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(5000); // let scheduled logs print
        executor.shutdown();
    }
}
