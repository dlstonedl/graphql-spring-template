package com.dlstone.graphql.controller;

import com.dlstone.graphql.common.GraphqlClient;
import com.dlstone.graphql.common.GraphqlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphQLController {

    private GraphqlClient graphqlClient;

    @Autowired
    public GraphQLController(GraphqlClient graphqlClient) {
        this.graphqlClient = graphqlClient;
    }

    @PostMapping("/graphql")
    public Object graphql(@RequestBody GraphqlRequest graphqlRequest) {
        return graphqlClient.invoke(graphqlRequest);
    }
}
