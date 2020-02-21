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

    private BatchLoader<String, Map<String, String>> authorBatchLoader;

    @Autowired
    public GraphqlClient(GraphqlFactory graphqlFactory, BatchLoader<String, Map<String, String>> authorBatchLoader) {
        this.graphQL = graphqlFactory.getGraphQL();
        this.authorBatchLoader = authorBatchLoader;
    }

    public Object invoke(GraphqlRequest graphqlRequest) {
        DataLoaderRegistry loaderRegistry = new DataLoaderRegistry();
        loaderRegistry.register(GraphQLDataLoader.AUTHOR_LOADER, DataLoader.newDataLoader(authorBatchLoader));

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphqlRequest.getQuery())
            .operationName(graphqlRequest.getOperationName())
            .variables(graphqlRequest.getVariables() != null ? graphqlRequest.getVariables() : Collections.emptyMap())
            .dataLoaderRegistry(loaderRegistry)
            .build();
        return this.graphQL.executeAsync(executionInput).thenApply(ExecutionResult::toSpecification);
    }
}
