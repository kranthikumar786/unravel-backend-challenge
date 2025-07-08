package session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryControllerTest {

    private MemoryManager memoryManager;
    private MemoryController controller;

    @BeforeEach
    public void setup() {
        memoryManager = mock(MemoryManager.class);
        controller = new MemoryController(memoryManager);
    }

    @Test
    public void testAddSessionData() {
        String sessionId = "kranthi";

        String result = controller.add(sessionId);

        verify(memoryManager).addSessionData(sessionId);
        assertEquals("Session data added for: kranthi", result);
    }

    @Test
    public void testGetSessionDataExists() {
        String sessionId = "kranthi";
        when(memoryManager.getSessionData(sessionId)).thenReturn(new byte[10]);

        String result = controller.get(sessionId);

        verify(memoryManager).getSessionData(sessionId);
        assertEquals("Data exists for: kranthi", result);
    }

    @Test
    public void testGetSessionDataNotFound() {
        String sessionId = "arjun";
        when(memoryManager.getSessionData(sessionId)).thenReturn(null);

        String result = controller.get(sessionId);

        verify(memoryManager).getSessionData(sessionId);
        assertEquals("No session data found.", result);
    }

    @Test
    public void testRemoveSessionData() {
        String sessionId = "ravi";

        String result = controller.remove(sessionId);

        verify(memoryManager).removeSessionData(sessionId);
        assertEquals("Session data removed for: ravi", result);
    }
}
