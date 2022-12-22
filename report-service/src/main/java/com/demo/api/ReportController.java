package com.demo.api;

import com.demo.report.Report;
import com.demo.report.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/v1")
public class ReportController {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private PagedResourcesAssembler<Report> pagedResourcesAssembler;

    @Autowired
    private ReportAssembler reportAssembler;

    @GetMapping(value = "/reports")
    public ResponseEntity<PagedModel<EntityModel<Report>>> all(Pageable pageable) {

        Page<Report> reports = reportDao.findAll(pageable);
        throw new RuntimeException();
        //return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(pagedResourcesAssembler.toModel(reports, reportAssembler));
    }

    @GetMapping(value = "/reports/{id}")
    public ResponseEntity<EntityModel<Report>> get(@PathVariable Long id) {
        Report report = reportDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return ResponseEntity.ok(reportAssembler.toModel(report));
    }

}
