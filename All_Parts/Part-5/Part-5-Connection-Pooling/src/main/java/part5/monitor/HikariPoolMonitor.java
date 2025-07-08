package com.part5.monitor;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HikariPoolMonitor {

    private final HikariDataSource dataSource;

    public HikariPoolMonitor(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ HikariPoolMonitor initialized for pool: " + dataSource.getPoolName());
    }

    @Scheduled(fixedRate = 5000)
    public void monitor() {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();

        if (poolMXBean == null) {
            System.out.println("⏳ Hikari pool not ready yet. Skipping monitoring...");
            return;
        }

        int active = poolMXBean.getActiveConnections();
        int idle = poolMXBean.getIdleConnections();
        long waiting = poolMXBean.getThreadsAwaitingConnection();
        int max = dataSource.getMaximumPoolSize();

        System.out.printf("🔍 Pool Stats | Active: %d | Idle: %d | Waiting: %d | Max: %d%n",
                active, idle, waiting, max);

        if (waiting > 0) {
            System.out.println("⚠️  Warning: Threads are waiting for DB connections!");
        }

        if (idle == max) {
            System.out.println("ℹ️  Info: Pool is underutilized.");
        }
    }
}
