package session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.Duration;

@Service
public class RedisSessionManager {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisSessionManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String login(String userId) {
        String sessionId = "SESSION_" + UUID.randomUUID();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(userId, sessionId);

        if (Boolean.FALSE.equals(success)) {
            return "User already logged in. Session ID: " + redisTemplate.opsForValue().get(userId);
        }

        redisTemplate.expire(userId, Duration.ofMinutes(30));
        return "Login successful. Session ID: " + sessionId;
    }

    public String logout(String userId) {
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(userId))) {
            return "User not logged in.";
        }
        redisTemplate.delete(userId);
        return "Logout successful.";
    }

    public String getSessionDetails(String userId) {
        String session = redisTemplate.opsForValue().get(userId);
        if (session == null) {
            return "Session not found for user: " + userId;
        }
        return "Session ID for user " + userId + ": " + session;
    }
    public String loginWithCustomExpiry(String userId, Duration ttl) {
        String sessionId = "SESSION_" + UUID.randomUUID();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(userId, sessionId);
        if (Boolean.FALSE.equals(success)) {
            return "User already logged in. Session ID: " + redisTemplate.opsForValue().get(userId);
        }
        redisTemplate.expire(userId, ttl);
        return "Login successful. Session ID: " + sessionId;
    }

}