package com.dlstone.graphql.common;

import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Configuration
public class GraphQLDataLoader {

    public static final String AUTHOR_LOADER = "author-loader";

    private GraphQLData graphQLData;

    @Autowired
    public GraphQLDataLoader(GraphQLData graphQLData) {
        this.graphQLData = graphQLData;
    }

    @Bean
    public BatchLoader<String, Map<String, String>> authorBatchLoader() {
        return authorIds -> CompletableFuture.supplyAsync(() ->
            graphQLData.getBatchAuthors(authorIds));
    }
}
