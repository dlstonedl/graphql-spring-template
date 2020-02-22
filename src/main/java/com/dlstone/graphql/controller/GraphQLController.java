package com.dlstone.graphql.controller;

import com.dlstone.graphql.common.GraphQLClient;
import com.dlstone.graphql.common.GraphQLRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphQLController {

    private GraphQLClient graphqlClient;

    @Autowired
    public GraphQLController(GraphQLClient graphqlClient) {
        this.graphqlClient = graphqlClient;
    }

    @PostMapping("/graphql")
    public Object graphql(@RequestBody GraphQLRequest graphqlRequest) {
        return graphqlClient.invoke(graphqlRequest);
    }
}
