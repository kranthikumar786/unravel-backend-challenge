package session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RedisSessionManagerTest {

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private RedisSessionManager sessionManager;

    @BeforeEach
    public void setup() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        sessionManager = new RedisSessionManager(redisTemplate);
    }

    @Test
    public void testLogin_NewUser_ShouldSucceed() {
        String userId = "kranthi";
        when(valueOperations.setIfAbsent(eq(userId), anyString())).thenReturn(true);

        String result = sessionManager.login(userId);
        assertTrue(result.contains("Login successful"));

        verify(redisTemplate).expire(eq(userId), eq(Duration.ofMinutes(30)));
    }

    @Test
    public void testLogin_AlreadyLoggedIn_ShouldReturnExistingSession() {
        String userId = "kranthi";
        when(valueOperations.setIfAbsent(eq(userId), anyString())).thenReturn(false);
        when(valueOperations.get(userId)).thenReturn("SESSION_123");

        String result = sessionManager.login(userId);
        assertTrue(result.contains("User already logged in"));
    }

    @Test
    public void testGetSessionDetails_WhenSessionExists() {
        String userId = "kranthi";
        when(valueOperations.get(userId)).thenReturn("SESSION_abc");

        String result = sessionManager.getSessionDetails(userId);
        assertEquals("Session ID for user kranthi: SESSION_abc", result);
    }

    @Test
    public void testGetSessionDetails_WhenNoSession() {
        String userId = "kranthi";
        when(valueOperations.get(userId)).thenReturn(null);

        String result = sessionManager.getSessionDetails(userId);
        assertEquals("Session not found for user: kranthi", result);
    }

    @Test
    public void testLogout_WhenSessionExists() {
        String userId = "kranthi";
        when(redisTemplate.hasKey(userId)).thenReturn(true);

        String result = sessionManager.logout(userId);
        assertEquals("Logout successful.", result);
        verify(redisTemplate).delete(userId);
    }

    @Test
    public void testLogout_WhenSessionNotExists() {
        String userId = "kranthi";
        when(redisTemplate.hasKey(userId)).thenReturn(false);

        String result = sessionManager.logout(userId);
        assertEquals("User not logged in.", result);
    }

    @Test
    public void testMultipleUsers_IndependentSessions() {
        String userA = "kranthi";
        String userB = "arjun";

        when(valueOperations.setIfAbsent(eq(userA), anyString())).thenReturn(true);
        when(valueOperations.setIfAbsent(eq(userB), anyString())).thenReturn(true);

        String resultA = sessionManager.login(userA);
        String resultB = sessionManager.login(userB);

        assertTrue(resultA.contains("Login successful"));
        assertTrue(resultB.contains("Login successful"));

        verify(redisTemplate, times(2)).expire(anyString(), eq(Duration.ofMinutes(30)));
    }
}
