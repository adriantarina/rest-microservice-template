package com.demo.api;

import com.demo.report.Report;
import com.demo.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/v1")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PagedResourcesAssembler<Report> pagedResourcesAssembler;

    @Autowired
    private ReportAssembler reportAssembler;

    @GetMapping(value = "/reports")
    public ResponseEntity<PagedModel<EntityModel<Report>>> all(Pageable pageable) {

        Page<Report> reports = reportRepository.findAll(pageable);

        return ResponseEntity.ok((pagedResourcesAssembler.toModel(reports, reportAssembler)));
    }

    @GetMapping(value = "/reports/{id}")
    public ResponseEntity<EntityModel<Report>> get(@PathVariable Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return ResponseEntity.ok(reportAssembler.toModel(report));
    }

    @PostMapping(value = "/reports")
    public ResponseEntity<EntityModel<Report>> newReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportAssembler.toModel(reportRepository.save(report)));
    }

    @PutMapping(value = "/reports/{id}")
    public ResponseEntity<?> updateReport(@RequestBody Report newReport, @PathVariable Long id) {
        EntityModel<Report> reportModel = reportAssembler.toModel(reportRepository.findById(id)
                .map(report -> {
                    report.setReportName(newReport.getReportName());
                    report.setOwnerId(newReport.getOwnerId());
                    report.setSubmitterId(newReport.getSubmitterId());
                    return reportRepository.save(report);
                })
                .orElseGet(() -> {
                    newReport.setId(id);
                    return reportRepository.save(newReport);
                }));

        return ResponseEntity.ok(reportModel);
    }

    @DeleteMapping(value = "/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        reportRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
