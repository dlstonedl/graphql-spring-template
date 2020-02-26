package com.dlstone.graphql.util.common;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class GraphQLClient {

    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    @Autowired
    public GraphQLClient(GraphQLFactory graphqlFactory, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    public CompletableFuture<Map<String, Object>> invoke(GraphQLCommand graphQLCommand) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphQLCommand.getQuery())
            .operationName(graphQLCommand.getOperationName())
            .variables(graphQLCommand.getVariables())
            .context(graphQLCommand.getContext())
            .dataLoaderRegistry(dataLoaderRegistryFactory.newDataLoaderRegistry(graphQLCommand.getContext()))
            .build();
        return this.graphQL.executeAsync(executionInput).thenApply(ExecutionResult::toSpecification);
    }
}
