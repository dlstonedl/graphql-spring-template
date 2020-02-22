package com.dlstone.graphql.controller;

import com.dlstone.graphql.annotation.LoaderController;
import com.dlstone.graphql.annotation.LoaderMapping;
import com.dlstone.graphql.repository.GraphQLData;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@LoaderController
public class DataLoadController {

    private GraphQLData graphQLData;

    @Autowired
    public DataLoadController(GraphQLData graphQLData) {
        this.graphQLData = graphQLData;
    }

    @LoaderMapping(name = "author-loader")
    public BatchLoader<String, Map<String, String>> authorBatchLoader() {
        return authorIds -> graphQLData.getAuthorsByIds(authorIds).collectList().toFuture();
    }
}
