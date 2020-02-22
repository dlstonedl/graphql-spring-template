package com.dlstone.graphql.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GraphQLRequest {
    private String query;
    private String operationName;
    private Map<String, Object> variables;
}
