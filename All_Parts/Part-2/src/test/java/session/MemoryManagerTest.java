package session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryManagerTest {

    private MemoryManager memoryManager;
    private RedisTemplate<String, byte[]> redisTemplate;
    private ValueOperations<String, byte[]> valueOperations;

    @BeforeEach
    void setup() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        memoryManager = new MemoryManager(redisTemplate);
    }

    @Test
    void testAddSessionData() {
        String sessionId = "kranthi";

        memoryManager.addSessionData(sessionId);

        verify(valueOperations).set(eq(sessionId), any(byte[].class));
        verify(redisTemplate).expire(eq(sessionId), eq(Duration.ofMinutes(30)));
    }

    @Test
    void testGetSessionData() {
        String sessionId = "kranthi";
        byte[] expectedData = new byte[512];

        when(valueOperations.get(sessionId)).thenReturn(expectedData);

        byte[] actualData = memoryManager.getSessionData(sessionId);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    void testRemoveSessionData() {
        String sessionId = "kranthi";

        memoryManager.removeSessionData(sessionId);

        verify(redisTemplate).delete(eq(sessionId));
    }
}
