package com.demo.api.dto;

import com.demo.client.dto.User;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDate;

public record ReportDTO (Long id, String reportName, LocalDate createdDate, EntityModel<User> submitter, EntityModel<User> owner) {}
