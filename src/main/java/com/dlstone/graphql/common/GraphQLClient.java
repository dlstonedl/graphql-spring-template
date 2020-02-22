package com.dlstone.graphql.common;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GraphQLClient {

    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    @Autowired
    public GraphQLClient(GraphQLFactory graphqlFactory, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    public Object invoke(GraphQLRequest graphqlRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphqlRequest.getQuery())
            .operationName(graphqlRequest.getOperationName())
            .variables(graphqlRequest.getVariables() != null ? graphqlRequest.getVariables() : Collections.emptyMap())
            .dataLoaderRegistry(dataLoaderRegistryFactory.newDataLoaderRegistry())
            .build();
        return this.graphQL.executeAsync(executionInput).thenApply(ExecutionResult::toSpecification);
    }
}
