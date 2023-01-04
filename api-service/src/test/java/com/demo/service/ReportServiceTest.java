package com.demo.service;

import com.demo.api.dto.ReportDTO;
import com.demo.client.ReportClient;
import com.demo.client.UserClient;
import com.demo.client.dto.Report;
import com.demo.client.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportClient reportClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ReportService reportService = new ReportService();

    private final Pageable pageable = Pageable.ofSize(100);

    private final PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(),100);

    @Test
    public void getAll_exception() {

        when(reportClient.getReports(pageable)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> reportService.getAll(pageable));
    }

    @Test
    public void getAll_emptyReportList() {
        when(reportClient.getReports(pageable)).thenReturn(PagedModel.empty(pageMetadata));
        assertEquals(0, reportService.getAll(pageable).getNumberOfElements());
    }

    @Test
    public void getAll_oneReport() {
        Report report = new Report(1L, "reportName", 5L, 10L);
        when(reportClient.getReports(pageable)).thenReturn(PagedModel.of(List.of(EntityModel.of(report)), pageMetadata));
        when(userClient.getAll(anyList())).thenReturn(List.of(
                EntityModel.of(new User(5L, "John Snow", "OPS")),
                EntityModel.of(new User(10L, "Little Finger", "Manager"))));
        Page<ReportDTO> reports = reportService.getAll(pageable);
        verify(userClient, times(1)).getAll(List.of(5L, 10L));
        assertEquals(1, reports.getNumberOfElements());
        assertEquals(new ReportDTO(report.id(),report.reportName(),
                EntityModel.of(new User(5L, "John Snow", "OPS")),
                        EntityModel.of(new User(10L, "Little Finger", "Manager"))), reports.getContent().get(0));
    }

    @Test
    public void getAll_multipleReport() {

        User user1 = new User(5L, "John Snow", "OPS");
        User user2 = new User(10L, "Little Finger", "Manager");
        User user3 = new User(20L, "Arya Stark", "OPS");
        User user4 = new User(30L, "Tyrion Lannister", "Manager");

        Report report1 = new Report(1L, "reportName1", user1.id(), user2.id());
        Report report2 = new Report(2L, "reportName2", user3.id(), user4.id());
        when(reportClient.getReports(pageable)).thenReturn(PagedModel.of(List.of(EntityModel.of(report1), EntityModel.of(report2)), pageMetadata));

        when(userClient.getAll(anyList())).thenReturn(List.of(EntityModel.of(user1), EntityModel.of(user2), EntityModel.of(user3), EntityModel.of(user4)));

        Page<ReportDTO> reports = reportService.getAll(pageable);

        assertEquals(2, reports.getContent().size());

        List<ReportDTO> returnedReports = List.of(
                buildReportDTO(report1, user1, user2),
                buildReportDTO(report2, user3, user4));

        assertEquals(returnedReports, reports.getContent());
    }

    private ReportDTO buildReportDTO(Report report, User submitter, User owner) {
        return new ReportDTO(report.id(), report.reportName(), EntityModel.of(submitter), EntityModel.of(owner));
    }
}
