package com.dlstone.graphql.controller;

import com.dlstone.graphql.common.GraphqlFactory;
import com.dlstone.graphql.common.GraphqlRequest;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class GraphQLController {

    private GraphQL graphQL;

    @Autowired
    public GraphQLController(GraphqlFactory graphqlFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
    }

    @PostMapping("/graphql")
    public Object graphql(@RequestBody GraphqlRequest graphqlRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphqlRequest.getQuery())
            .operationName(graphqlRequest.getOperationName())
            .variables(graphqlRequest.getVariables())
            .build();
        CompletableFuture<ExecutionResult> executeAsync = this.graphQL.executeAsync(executionInput);
        return executeAsync.thenApply(ExecutionResult::toSpecification);
    }
}
