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
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;
import java.util.Collections;
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

    @Test
    public void getAll_exception() {
        when(reportClient.getReports()).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> reportService.getAll());
    }

    @Test
    public void getAll_emptyReportList() {
        when(reportClient.getReports()).thenReturn(Collections.emptyList());
        assertEquals(0, reportService.getAll().size());
    }

    @Test
    public void getAll_oneReport() {
        Report report = new Report(1L, "reportName", LocalDate.now(), 5L, 10L);
        when(reportClient.getReports()).thenReturn(List.of(EntityModel.of(report)));
        when(userClient.getAll(anyList())).thenReturn(List.of(
                EntityModel.of(new User(5L, "John Snow", "OPS")),
                EntityModel.of(new User(10L, "Little Finger", "Manager"))));
        List<ReportDTO> reports = reportService.getAll();
        verify(userClient, times(1)).getAll(List.of(5L, 10L));
        assertEquals(1, reports.size());
        assertEquals(new ReportDTO(report.id(),report.reportName(), report.createdDate(),
                EntityModel.of(new User(5L, "John Snow", "OPS")),
                        EntityModel.of(new User(10L, "Little Finger", "Manager"))), reports.get(0));
    }

    @Test
    public void getAll_multipleReport() {
        LocalDate reportDate = LocalDate.now();

        User user1 = new User(5L, "John Snow", "OPS");
        User user2 = new User(10L, "Little Finger", "Manager");
        User user3 = new User(20L, "Arya Stark", "OPS");
        User user4 = new User(30L, "Tyrion Lannister", "Manager");

        Report report1 = new Report(1L, "reportName1", reportDate, user1.id(), user2.id());
        Report report2 = new Report(2L, "reportName2", reportDate, user3.id(), user4.id());
        when(reportClient.getReports()).thenReturn(List.of(EntityModel.of(report1), EntityModel.of(report2)));

        when(userClient.getAll(anyList())).thenReturn(List.of(EntityModel.of(user1), EntityModel.of(user2), EntityModel.of(user3), EntityModel.of(user4)));

        List<ReportDTO> reports = reportService.getAll();

        assertEquals(2, reports.size());

        List<ReportDTO> returnedReports = List.of(
                buildReportDTO(report1, user1, user2),
                buildReportDTO(report2, user3, user4));

        assertEquals(returnedReports, reports);
    }

    private ReportDTO buildReportDTO(Report report, User submitter, User owner) {
        return new ReportDTO(report.id(), report.reportName(), report.createdDate(), EntityModel.of(submitter), EntityModel.of(owner));
    }
}
