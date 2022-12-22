package com.demo;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;
import org.springframework.hateoas.config.HypermediaWebClientConfigurer;
import org.springframework.hateoas.support.WebStack;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Duration;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class APIServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(APIServiceApplication.class, args);
    }

    @Bean
    WebClientCustomizer hypermediaWebClientCustomizer(HypermediaWebClientConfigurer configurer) {
        return webClientBuilder -> {
            configurer.registerHypermediaTypes(webClientBuilder);
        };
    }

    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(HypermediaRestTemplateConfigurer configurer) {
        return restTemplate -> {
            configurer.registerHypermediaTypes(restTemplate);
        };
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .minimumNumberOfCalls(10)
                .waitDurationInOpenState(Duration.ofSeconds(100))
                .slidingWindowSize(2)
                .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
}
