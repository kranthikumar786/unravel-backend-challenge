package session;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memory")
public class MemoryController {

    private final MemoryManager memoryManager;

    public MemoryController(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    @PostMapping("/add/{sessionId}")
    public String add(@PathVariable String sessionId) {
        memoryManager.addSessionData(sessionId);
        return "Session data added for: " + sessionId;
    }

    @GetMapping("/get/{sessionId}")
    public String get(@PathVariable String sessionId) {
        byte[] data = memoryManager.getSessionData(sessionId);
        return data != null ? "Data exists for: " + sessionId : "No session data found.";
    }

    @DeleteMapping("/remove/{sessionId}")
    public String remove(@PathVariable String sessionId) {
        memoryManager.removeSessionData(sessionId);
        return "Session data removed for: " + sessionId;
    }
}
