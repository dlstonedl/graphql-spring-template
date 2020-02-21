package com.dlstone.graphql.common;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphqlClient {

    private GraphQL graphQL;

    @Autowired
    public GraphqlClient(GraphqlFactory graphqlFactory) {
        this.graphQL = graphqlFactory.getGraphQL();
    }

    public Object invoke(GraphqlRequest graphqlRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphqlRequest.getQuery())
            .operationName(graphqlRequest.getOperationName())
            .variables(graphqlRequest.getVariables())
            .build();
        return this.graphQL.executeAsync(executionInput).thenApply(ExecutionResult::toSpecification);
    }
}
