package session;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisSessionIntegrationTest {

    @Autowired
    private RedisSessionManager sessionManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    public void setup() {
        redisTemplate.delete("kranthi");
    }

    @AfterEach
    public void cleanup() {
        redisTemplate.delete("kranthi");
    }

    @Test
    public void testLoginAndFetchSession() {
        String result = sessionManager.login("kranthi");
        System.out.println("Login result: " + result);
        assertTrue(result.contains("Login successful"));

        String sessionDetails = sessionManager.getSessionDetails("kranthi");
        System.out.println("Session details: " + sessionDetails);
        assertTrue(sessionDetails.contains("Session ID for user kranthi:"));
    }

    @Test
    public void testLogoutClearsSession() {
        sessionManager.login("kranthi");

        String logoutResult = sessionManager.logout("kranthi");
        assertEquals("Logout successful.", logoutResult);

        String sessionDetails = sessionManager.getSessionDetails("kranthi");
        assertEquals("Session not found for user: kranthi", sessionDetails);
    }

    @Test
    public void testSessionExpiresAutomatically() throws InterruptedException {
        sessionManager.loginWithCustomExpiry("kranthi", Duration.ofSeconds(5));

        String session = sessionManager.getSessionDetails("kranthi");
        assertTrue(session.contains("Session ID for user kranthi"));

        Thread.sleep(6000); // wait for TTL to expire

        String afterTTL = sessionManager.getSessionDetails("kranthi");
        assertEquals("Session not found for user: kranthi", afterTTL);
    }
}
