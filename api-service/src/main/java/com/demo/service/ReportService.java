package com.demo.service;

import com.demo.api.dto.ReportDTO;
import com.demo.client.ReportClient;
import com.demo.client.UserClient;
import com.demo.client.dto.Report;
import com.demo.client.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReportService {

    @Autowired
    private ReportClient reportClient;

    @Autowired
    private UserClient userClient;

    public Page<ReportDTO> getAll(Pageable pageable) {
        PagedModel<EntityModel<Report>> reports = reportClient.getReports(pageable);

        return new PageImpl<>(toReportDTOs(reports.getContent()), pageable, reports.getMetadata().getTotalElements());
    }

    public ReportDTO save(Report report) {
        EntityModel<Report> savedReport = reportClient.saveReport(report);
        return toReportDTO(savedReport);
    }

    public ReportDTO update(Report report, Long id) {
        EntityModel<Report> updatedReport = reportClient.updateReport(report, id);
        return toReportDTO(updatedReport);
    }

    public ReportDTO get(Long id) {
        EntityModel<Report> updatedReport = reportClient.getReport(id);
        return toReportDTO(updatedReport);
    }

    public void deleteById(Long id) {
        reportClient.deleteReport(id);
    }

    private ReportDTO toReportDTO(EntityModel<Report> report) {
        return toReportDTOs(Collections.singletonList(report)).iterator().next();
    }
    private List<ReportDTO> toReportDTOs(Collection<EntityModel<Report>> reports) {
        Collection<EntityModel<User>> users = userClient.getAll(getUserIdsFromReports(reports));

        return reports.stream()
                .map(EntityModel::getContent)
                .map(report -> new ReportDTO(report.id(), report.reportName(),
                        getSubmitter(users, report.submitterId()), getSubmitter(users, report.ownerId())))
                .collect(Collectors.toList());
    }

    private static EntityModel<User> getSubmitter(Collection<EntityModel<User>> users, Long userId) {
        return users.stream()
                .filter(user -> Objects.equals(user.getContent().id(), userId)).findFirst().orElseThrow();
    }

    private List<Long> getUserIdsFromReports(Collection<EntityModel<Report>> reports) {
        return reports.stream()
                .map(report -> Arrays.asList(report.getContent().submitterId(), report.getContent().ownerId()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
