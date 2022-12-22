package com.demo.client;

import com.demo.client.dto.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder.clone().baseUrl("http://localhost:8081").build();
    }

    public Collection<EntityModel<User>> getAll(List<Long> userIds) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/users").queryParam("userIds", userIds).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CollectionModel<EntityModel<User>>>() {})
                .block().getContent();
    }

    private static String getUserIdsAsString(List<Long> userIds) {
        return userIds.stream()
                .map((userId) -> String.valueOf(userId))
                .collect(Collectors.joining(","));
    }
}
