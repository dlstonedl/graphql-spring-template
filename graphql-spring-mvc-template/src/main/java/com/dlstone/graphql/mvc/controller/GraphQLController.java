package com.dlstone.graphql.mvc.controller;

import com.dlstone.graphql.util.common.GraphQLClient;
import com.dlstone.graphql.util.common.GraphQLRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class GraphQLController {

    private GraphQLClient graphqlClient;

    @Autowired
    public GraphQLController(GraphQLClient graphqlClient) {
        this.graphqlClient = graphqlClient;
    }

    @PostMapping("/graphql")
    public ResponseEntity graphql(@RequestBody GraphQLRequest graphqlRequest) {
        return graphqlClient.invoke(graphqlRequest)
            .handle((result, throwable) ->
                Objects.isNull(throwable) ? ok(result) : badRequest().build())
            .join();
    }
}
