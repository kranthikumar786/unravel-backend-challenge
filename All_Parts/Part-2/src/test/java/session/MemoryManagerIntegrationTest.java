package session;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoryManagerIntegrationTest {

    @Autowired
    MemoryManager memoryManager;

    @Test
    void testAddAndGetAndRemove() {
        String sessionId = "test-session";
        memoryManager.addSessionData(sessionId);

        assertNotNull(memoryManager.getSessionData(sessionId));
        memoryManager.removeSessionData(sessionId);
        assertNull(memoryManager.getSessionData(sessionId));
    }
}
