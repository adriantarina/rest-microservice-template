package com.demo.client;

import com.demo.client.dto.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReportClient {

    private static final Logger LOG =  LoggerFactory.getLogger(ReportClient.class);

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public ReportClient(WebClient.Builder builder, CircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = builder.clone().baseUrl("http://localhost:8082").build();
        this.circuitBreaker = circuitBreakerFactory.create("reportClient");
    }

    public PagedModel<EntityModel<Report>> getReports(Pageable pageable) {
        return circuitBreaker.run(() -> webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/reports")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PagedModel<EntityModel<Report>>>() {})
                .block(), (throwable) -> {
            LOG.warn("Error making request to report service", throwable);
            return PagedModel.empty();});

    }

    public EntityModel<Report> getReport(Long reportId) {
        return webClient.get().uri("/api/v1/reports/{id}", reportId)
                .accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(new ParameterizedTypeReference<EntityModel<Report>>() {}).block();

    }

    public EntityModel<Report> saveReport(Report report) {
        return webClient.post().uri("/api/v1/reports")
                .body(Mono.just(report), Report.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EntityModel<Report>>() {}).block();
    }

    public EntityModel<Report> updateReport(Report report, Long id) {
        return webClient.put().uri("/api/v1/reports/{id}", id)
                .body(Mono.just(report), Report.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EntityModel<Report>>() {}).block();
    }

    public void deleteReport(Long reportId) {
        webClient.delete().uri("/api/v1/reports/{id}", reportId)
                .retrieve()
                .bodyToMono(Void.class).block();
    }
}
