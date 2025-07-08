package comparator;

import model.LogTask;

import java.util.Comparator;

public class LogTaskComparator {
    public static final Comparator<LogTask> BY_PRIORITY_THEN_TIMESTAMP = (t1, t2) -> {
        int priorityCompare = Integer.compare(t1.getPriority(), t2.getPriority());
        return priorityCompare != 0
                ? priorityCompare
                : t1.getCreatedAt().compareTo(t2.getCreatedAt());
    };
}
/*
 * If higher priority then that will be returned,
 * or else if both are having same priority then the one created first
 * will be given top priority — so we are avoiding starvation.
 */
