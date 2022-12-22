package com.demo.report;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "report")
public class Report {

    @Id @GeneratedValue
    private Long id;

    private String reportName;

    private Long submitterId;

    private Long ownerId;

    public Report(String reportName, Long submitterId, Long ownerId) {
        this.reportName = reportName;
        this.submitterId = submitterId;
        this.ownerId = ownerId;
    }

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id.equals(report.id) && reportName.equals(report.reportName) && submitterId.equals(report.submitterId) && ownerId.equals(report.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportName, submitterId, ownerId);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportName='" + reportName + '\'' +
                ", submitterId=" + submitterId +
                ", ownerId=" + ownerId +
                '}';
    }
}
