package com.dlstone.graphql.controller;

import com.dlstone.graphql.annotation.LoaderController;
import com.dlstone.graphql.annotation.LoaderMapping;
import com.dlstone.graphql.repository.GraphQLRepository;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@LoaderController
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
