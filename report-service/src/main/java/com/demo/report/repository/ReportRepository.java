package com.demo.report.repository;

import com.demo.report.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> {

}
