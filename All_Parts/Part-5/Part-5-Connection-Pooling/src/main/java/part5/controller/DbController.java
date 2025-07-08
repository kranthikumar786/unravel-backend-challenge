package com.part5.controller;

import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/db")
public class DbController {

    private final DataSource dataSource;

    public DbController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/query")
    public String query(@RequestParam(defaultValue = "1000") long delayMs) throws SQLException, InterruptedException {
        try (Connection connection = dataSource.getConnection()) {
            TimeUnit.MILLISECONDS.sleep(delayMs);
            return "Query done using connection from pool";
        }
    }
}
