package com.dlstone.graphql.util.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
public class GraphQLCommand {
    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;
    @Setter
    private Object context;

    public GraphQLCommand(GraphQLRequest graphQLRequest) {
        this.query = graphQLRequest.getQuery();
        this.operationName = graphQLRequest.getOperationName();
        this.variables = graphQLRequest.getVariables() != null ? graphQLRequest.getVariables() : Collections.emptyMap();
    }
}
