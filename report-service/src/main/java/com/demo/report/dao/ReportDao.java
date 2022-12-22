package com.demo.report.dao;

import com.demo.report.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportDao extends PagingAndSortingRepository<Report, Long> {



}
