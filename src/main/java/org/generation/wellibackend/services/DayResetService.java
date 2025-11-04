package org.generation.wellibackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DayResetService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyData() {
        String query = "UPDATE user SET first_daily_access = true, extra_data = '{}';";
        System.out.println("\n\n --> MODIFICA QUERY PROGRAMMATA:" +
                           "\n        RESET COLUMN: First_daily_access & Extra_Data = " + jdbcTemplate.update(query));
        query = "truncate table mood;";
        System.out.println("\n\n --> MODIFICA QUERY PROGRAMMATA:" +
                "\n        RESET TABLE: mood = " + jdbcTemplate.update(query));
        query = "truncate table motivational_phrase;";
        System.out.println("\n\n --> MODIFICA QUERY PROGRAMMATA:" +
                "\n        RESET TABLE: motivational_phrase = " + jdbcTemplate.update(query));
    }
}
