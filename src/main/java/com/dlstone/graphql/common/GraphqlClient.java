package com.dlstone.graphql.common;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class GraphqlClient {

    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    @Autowired
    public GraphqlClient(GraphqlFactory graphqlFactory, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    public Object invoke(GraphqlRequest graphqlRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphqlRequest.getQuery())
            .operationName(graphqlRequest.getOperationName())
            .variables(graphqlRequest.getVariables() != null ? graphqlRequest.getVariables() : Collections.emptyMap())
            .dataLoaderRegistry(dataLoaderRegistryFactory.newDataLoaderRegistry())
            .build();
        return this.graphQL.executeAsync(executionInput).thenApply(ExecutionResult::toSpecification);
    }
}
