package session;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final RedisSessionManager sessionManager;

    public SessionController(RedisSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @PostMapping("/login/{userId}")
    public String login(@PathVariable String userId) {
        return sessionManager.login(userId);
    }

    @PostMapping("/logout/{userId}")
    public String logout(@PathVariable String userId) {
        return sessionManager.logout(userId);
    }

    @GetMapping("/{userId}")
    public String getSession(@PathVariable String userId) {
        return sessionManager.getSessionDetails(userId);
    }
}