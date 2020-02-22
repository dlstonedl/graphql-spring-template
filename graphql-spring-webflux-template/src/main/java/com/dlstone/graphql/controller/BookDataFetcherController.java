package com.dlstone.graphql.controller;


import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.repository.GraphQLData;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@FetcherController
public class BookDataFetcherController {

    private GraphQLData graphQLData;

    @Autowired
    public BookDataFetcherController(GraphQLData graphQLData) {
        this.graphQLData = graphQLData;
    }

    @FetcherMapping(typeName = "Query", fileName = "bookById")
    public DataFetcher getBookByIdDataFetcher() {
        return environment -> {
            String id = environment.getArgument("id");
            return graphQLData.getBookById(id).toFuture();
        };
    }

    @FetcherMapping(typeName = "Query", fileName = "books")
    public DataFetcher getBooks() {
        return environment -> graphQLData.getAllBooks().collectList().toFuture();
    }

    @FetcherMapping(typeName = "Mutation", fileName = "updateBook")
    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            return graphQLData.updateBook(newBook).toFuture();
        };
    }
}
