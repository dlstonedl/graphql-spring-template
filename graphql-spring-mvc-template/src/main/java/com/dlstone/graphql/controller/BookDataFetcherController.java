package com.dlstone.graphql.controller;


import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.repository.GraphQLRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@FetcherController
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
            return graphQLRepository.getBookById(id);
        };
    }

    @FetcherMapping(typeName = "Query", fileName = "books")
    public DataFetcher getBooks() {
        return environment -> graphQLRepository.getAllBooks();
    }

    @FetcherMapping(typeName = "Mutation", fileName = "updateBook")
    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            graphQLRepository.updateBook(newBook);
            return newBook;
        };
    }
}
