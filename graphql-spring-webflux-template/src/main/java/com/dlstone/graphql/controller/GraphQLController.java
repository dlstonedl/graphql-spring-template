package com.dlstone.graphql.controller;

import com.dlstone.graphql.common.DataLoaderRegistryFactory;
import com.dlstone.graphql.common.GraphQLFactory;
import com.dlstone.graphql.common.GraphQLRequest;
import graphql.ExecutionInput;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class GraphQLController {

    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    @Autowired
    public GraphQLController(GraphQLFactory graphqlFactory, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    @PostMapping("/graphql")
    public Mono<ResponseEntity<Map<String, Object>>> graphql(@RequestBody Mono<GraphQLRequest> graphQLRequestMono) {
        return graphQLRequestMono
            .map(graphQLRequest -> ExecutionInput.newExecutionInput()
                    .query(graphQLRequest.getQuery())
                    .operationName(graphQLRequest.getOperationName())
                    .variables(graphQLRequest.getVariables() != null ? graphQLRequest.getVariables() : Collections.emptyMap())
                    .dataLoaderRegistry(dataLoaderRegistryFactory.newDataLoaderRegistry())
                    .build())
            .flatMap(executionInput -> Mono.fromFuture(this.graphQL.executeAsync(executionInput)))
            .map(executionResult -> ResponseEntity.ok().body(executionResult.toSpecification()))
            .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

}
