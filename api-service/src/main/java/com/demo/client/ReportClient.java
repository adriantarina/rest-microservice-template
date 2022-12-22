package com.demo.client;

import com.demo.client.dto.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Collections;

@Component
public class ReportClient {

    private static Logger LOG =  LoggerFactory.getLogger(ReportClient.class);

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public ReportClient(WebClient.Builder builder, CircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = builder.clone().baseUrl("http://localhost:8082").build();
        this.circuitBreaker = circuitBreakerFactory.create("reportClient");
    }

    public Collection<EntityModel<Report>> getReports() {
        return circuitBreaker.run(() -> webClient.get().uri("/api/v1/reports")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CollectionModel<EntityModel<Report>>>() {})
                .block().getContent(), (throwable) -> {
            System.out.println("test"); LOG.warn("Error making request to report service", throwable);
            return Collections.emptyList();});

    }

    public Report getReport(Long reportId) {
        return webClient.get().uri("/api/v1/report/{id}", reportId)
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(Report.class).block();

    }

}
