package session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class MemoryManager {

    private final RedisTemplate<String, byte[]> redisTemplate;

    public MemoryManager(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addSessionData(String sessionId) {
        redisTemplate.opsForValue().set(sessionId, new byte[10 * 1024 * 1024]);
        redisTemplate.expire(sessionId, Duration.ofMinutes(30)); // TTL to avoid memory bloat
    }

    public byte[] getSessionData(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }

    public void removeSessionData(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
