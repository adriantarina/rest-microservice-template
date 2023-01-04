package com.demo.api;

import com.demo.api.dto.ReportDTO;
import com.demo.client.dto.Report;
import com.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReportController {


    @Autowired
    private ReportAssembler reportAssembler;

    @Autowired
    private PagedResourcesAssembler<ReportDTO> pagedResourcesAssembler;

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<ReportDTO> reports = reportService.getAll(pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(reports, reportAssembler));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(reportAssembler.toModel(reportService.get(id)));
    }

    @PostMapping(value = "/reports")
    public ResponseEntity<?> newReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportAssembler.toModel(reportService.save(report)));
    }

    @PutMapping(value = "/reports/{id}")
    public ResponseEntity<?> updateReport(@RequestBody Report report, @PathVariable Long id) {
        return ResponseEntity.ok(reportAssembler.toModel(reportService.update(report, id)));
    }

    @DeleteMapping(value = "/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        reportService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
