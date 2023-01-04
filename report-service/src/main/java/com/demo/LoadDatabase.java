package com.demo;

import com.demo.report.Report;
import com.demo.report.repository.ReportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ReportRepository reportRepository) {
        return args -> {
            reportRepository.save(new Report("Transaction Report1", 1l, 2l));
            reportRepository.save(new Report("Transaction Report2", 1l, 2l));
        };
    }
}
