package com.demo.api;

import com.demo.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReportAssembler implements RepresentationModelAssembler<Report, EntityModel<Report>> {

    @Override
    public EntityModel<Report> toModel(Report report) {
        return EntityModel.of(report,
                linkTo(methodOn(ReportController.class).get(report.getId())).withSelfRel(),
                linkTo(methodOn(ReportController.class).updateReport(report, report.getId())).withRel("edit"),
                linkTo(methodOn(ReportController.class).deleteReport(report.getId())).withRel("delete"),
                linkTo(methodOn(ReportController.class).all(Page.empty().getPageable())).withRel("list"));
    }

}
