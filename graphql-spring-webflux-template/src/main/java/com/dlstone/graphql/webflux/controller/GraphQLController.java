package com.dlstone.graphql.webflux.controller;

import com.dlstone.graphql.util.common.GraphQLClient;
import com.dlstone.graphql.util.common.GraphQLRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
public class GraphQLController {

    private GraphQLClient graphQLClient;

    @Autowired
    public GraphQLController(GraphQLClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }

    @PostMapping("/graphql")
    public Mono<ResponseEntity<Map<String, Object>>> graphql(@RequestBody Mono<GraphQLRequest> graphQLRequestMono) {
        return graphQLRequestMono
            .flatMap(graphQLRequest -> Mono.fromFuture(graphQLClient.invoke(graphQLRequest)))
            .map(result -> ResponseEntity.ok().body(result))
            .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }
}
