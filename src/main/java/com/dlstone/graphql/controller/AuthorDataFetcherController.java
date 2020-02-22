package com.dlstone.graphql.controller;

import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.common.GraphQLData;
import com.dlstone.graphql.common.GraphQLDataLoader;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@FetcherController
public class AuthorDataFetcherController {

    private GraphQLData graphQLData;

    @Autowired
    public AuthorDataFetcherController(GraphQLData graphQLData) {
        this.graphQLData = graphQLData;
    }

    @FetcherMapping(typeName = "Book", fileName = "authors")
    public DataFetcher getAuthorLoaderDataFetcher() {
        return environment -> {
            Map<String, Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            DataLoader<String, Map<String, String>> authorDataLoader = environment.getDataLoader(GraphQLDataLoader.AUTHOR_LOADER);
            return authorDataLoader.loadMany(authorIds);
        };
    }

//    @FetcherMapping(typeName = "Book", fileName = "authors")
    public DataFetcher getAuthorDataFetcher() {
        return environment -> {
            Map<String,Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            log.info("authorIds: " + authorIds.toString());

            return graphQLData.getAuthors()
                .stream()
                .filter(author -> authorIds.contains(author.get("id")))
                .collect(Collectors.toList());
        };
    }
}
