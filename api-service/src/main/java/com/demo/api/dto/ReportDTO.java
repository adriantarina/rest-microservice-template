package com.demo.api.dto;

import com.demo.client.dto.User;
import org.springframework.hateoas.EntityModel;

public record ReportDTO (Long id, String reportName, EntityModel<User> submitter, EntityModel<User> owner) {}
