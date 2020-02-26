package com.dlstone.graphql.webflux.controller;


import com.dlstone.graphql.util.annotation.GraphQLController;
import com.dlstone.graphql.util.annotation.FetcherMapping;
import com.dlstone.graphql.webflux.repository.GraphQLRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@GraphQLController
public class BookDataFetcherController {

    private GraphQLRepository graphQLRepository;

    @Autowired
    public BookDataFetcherController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

    @FetcherMapping(typeName = "Query", fileName = "bookById")
    public DataFetcher getBookByIdDataFetcher() {
        return environment -> {
            String id = environment.getArgument("id");
            return graphQLRepository.getBookById(id).toFuture();
        };
    }

    @FetcherMapping(typeName = "Query", fileName = "books")
    public DataFetcher getBooks() {
        return environment -> graphQLRepository.getAllBooks().collectList().toFuture();
    }

    @FetcherMapping(typeName = "Mutation", fileName = "updateBook")
    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            return graphQLRepository.updateBook(newBook).toFuture();
        };
    }
}
