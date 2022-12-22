package com.demo.service;

import com.demo.api.dto.ReportDTO;
import com.demo.client.ReportClient;
import com.demo.client.UserClient;
import com.demo.client.dto.Report;
import com.demo.client.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReportService {

    @Autowired
    private ReportClient reportClient;

    @Autowired
    private UserClient userClient;

    public List<ReportDTO> getAll() {
        Collection<EntityModel<Report>> reports = reportClient.getReports();
        if (reports.size() == 0) {
            return Collections.emptyList();
        }

        Collection<EntityModel<User>> users = userClient.getAll(getUserIdsFromReports(reports));

        return reports.stream()
                .map(reportEntityModel -> reportEntityModel.getContent())
                .map(report -> new ReportDTO(report.id(), report.reportName(), report.createdDate(),
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
