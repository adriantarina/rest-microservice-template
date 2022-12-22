package com.demo.api;

import com.demo.api.dto.ReportDTO;
import com.demo.service.ReportService;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public CollectionModel<EntityModel<ReportDTO>> getAll() {
        List<ReportDTO> reports = reportService.getAll();
        Page<ReportDTO> reportDTOS = new PageImpl<>(reports);

        return pagedResourcesAssembler.toModel(reportDTOS, reportAssembler);
    }

    @GetMapping("/reports/{id}")
    public EntityModel<ReportDTO> get(@PathVariable Long id) {
        return EntityModel.of(new ReportDTO(1L, "", null, null, null));
    }
}
