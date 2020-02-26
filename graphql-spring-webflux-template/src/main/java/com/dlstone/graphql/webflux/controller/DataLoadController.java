package com.dlstone.graphql.webflux.controller;

import com.dlstone.graphql.util.annotation.GraphQLController;
import com.dlstone.graphql.util.annotation.LoaderMapping;
import com.dlstone.graphql.webflux.repository.GraphQLRepository;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@GraphQLController
public class DataLoadController {

    private GraphQLRepository graphQLRepository;

    @Autowired
    public DataLoadController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

    @LoaderMapping(name = "author-loader")
    public BatchLoader<String, Map<String, String>> authorBatchLoader() {
        return authorIds -> graphQLRepository.getAuthorsByIds(authorIds).collectList().toFuture();
    }
}
