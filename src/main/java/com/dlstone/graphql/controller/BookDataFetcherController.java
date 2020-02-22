package com.dlstone.graphql.controller;


import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.common.GraphQLDataFetchers;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@FetcherController
public class BookDataFetcherController {

    private GraphQLDataFetchers graphQLDataFetchers;

    @Autowired
    public BookDataFetcherController(GraphQLDataFetchers graphQLDataFetchers) {
        this.graphQLDataFetchers = graphQLDataFetchers;
    }

    @FetcherMapping(typeName = "Query", fileName = "bookById")
    public DataFetcher getBookByIdDataFetcher() {
        return environment -> {
            String id = environment.getArgument("id");
            return graphQLDataFetchers.getBooks()
                .stream()
                .filter(book -> Objects.equals(book.get("id"), id))
                .findFirst()
                .orElse(null);
        };
    }

    @FetcherMapping(typeName = "Query", fileName = "books")
    public DataFetcher getBooks() {
        return environment -> graphQLDataFetchers.getBooks();
    }

    @FetcherMapping(typeName = "Mutation", fileName = "updateBook")
    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            List<Map<String, Object>> books = graphQLDataFetchers.getBooks()
                .stream()
                .filter(book -> !Objects.equals(book.get("id"), newBook.get("id")))
                .collect(Collectors.toList());
            books.add(newBook);
            graphQLDataFetchers.setBooks(books);
            return newBook;
        };
    }
}
