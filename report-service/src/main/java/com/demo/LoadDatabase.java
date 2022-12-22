package com.demo;

import com.demo.report.Report;
import com.demo.report.dao.ReportDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ReportDao reportDao) {
        return args -> {
            reportDao.save(new Report("Transaction Report", 1l, 2l));
            reportDao.save(new Report("username2", 1l, 2l));
        };
    }
}
