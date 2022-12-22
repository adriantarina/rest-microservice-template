package com.demo.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import java.time.LocalDate;

public record Report (Long id, String reportName, LocalDate createdDate, Long submitterId, Long ownerId) {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        var report = """
                {
                    "id": 1,
                    "reportName": "report1",
                    "createdDate": 123123123,
                    "submitterId": 1,
                    "ownerId": 2
                }
                """;

        var deser = objectMapper.readValue(report, Report.class);
        System.out.println(deser);
    }
}


