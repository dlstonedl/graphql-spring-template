package com.dlstone.graphql.mvc.controller;

import com.dlstone.graphql.util.annotation.GraphQLController;
import com.dlstone.graphql.util.annotation.LoaderMapping;
import com.dlstone.graphql.mvc.repository.GraphQLRepository;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.dataloader.BatchLoaderWithContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@GraphQLController
public class DataLoadController {

    private GraphQLRepository graphQLRepository;

    @Autowired
    public DataLoadController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

//    @LoaderMapping(name = "author-loader")
    public BatchLoader<String, Map<String, String>> authorBatchLoader() {
        return authorIds -> CompletableFuture.supplyAsync(() ->
            graphQLRepository.getAuthorsByIds(authorIds));
    }

    @LoaderMapping(name = "author-loader")
    public BatchLoaderWithContext<String, Map<String, String>> authorBatchLoaderWithContext() {
        return (keys, environment) ->  CompletableFuture.supplyAsync(() -> {
            Object context = environment.getContext();
            log.info("context: " + context);
            return graphQLRepository.getAuthorsByIds(keys);
        });
    }

}
