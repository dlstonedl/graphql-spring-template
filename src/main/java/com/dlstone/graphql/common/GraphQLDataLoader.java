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

    private GraphQLDataFetchers graphQLDataFetchers;

    @Autowired
    public GraphQLDataLoader(GraphQLDataFetchers graphQLDataFetchers) {
        this.graphQLDataFetchers = graphQLDataFetchers;
    }

    @Bean
    public BatchLoader<String, Map<String, String>> authorBatchLoader() {
        return authorIds -> CompletableFuture.supplyAsync(() ->
            graphQLDataFetchers.getBatchAuthors(authorIds));
    }
}
