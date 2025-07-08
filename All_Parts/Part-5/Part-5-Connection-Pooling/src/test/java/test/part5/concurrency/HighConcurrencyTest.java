package com.part5.concurrency;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HighConcurrencyTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:8081/api/users/ping";

    @Test
    void simulate50ConcurrentCalls() throws InterruptedException {
        int userCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(userCount);

        for (int i = 0; i < userCount; i++) {
            executor.submit(() -> {
                try {
                    String response = restTemplate.getForObject(url, String.class);
                    assertEquals("pong", response);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
