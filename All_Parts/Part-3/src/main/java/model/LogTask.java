package model;

import java.time.Instant;

public class LogTask {
    private final String message;
    private final String type;
    private final int priority;
    private final Instant createdAt; //

    public LogTask(String message, String type, int priority) {
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.createdAt = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + message + " (Priority: " + priority + ")";
    }
}
