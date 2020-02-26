package com.dlstone.graphql.mvc.controller;

import com.dlstone.graphql.util.annotation.GraphQLController;
import com.dlstone.graphql.util.annotation.FetcherMapping;
import com.dlstone.graphql.mvc.common.DataLoaderName;
import com.dlstone.graphql.mvc.repository.GraphQLRepository;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@GraphQLController
public class AuthorDataFetcherController {

    private GraphQLRepository graphQLRepository;

    @Autowired
    public AuthorDataFetcherController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

    @FetcherMapping(typeName = "Book", fileName = "authors")
    public DataFetcher getAuthorLoaderDataFetcher() {
        return environment -> {
            Map<String, Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            DataLoader<String, Map<String, String>> authorDataLoader = environment.getDataLoader(DataLoaderName.AUTHOR_LOADER.getValue());
            return authorDataLoader.loadMany(authorIds);
        };
    }

//    @FetcherMapping(typeName = "Book", fileName = "authors")
    public DataFetcher getAuthorDataFetcher() {
        return environment -> {
            Map<String,Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            log.info("authorIds: " + authorIds.toString());

            return graphQLRepository.getAuthorsByIds(authorIds);
        };
    }
}
