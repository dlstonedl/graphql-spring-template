package com.dlstone.graphql.controller;


import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.repository.GraphQLData;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            return graphQLData.getBooks()
                .stream()
                .filter(book -> Objects.equals(book.get("id"), id))
                .findFirst()
                .orElse(null);
        };
    }

    @FetcherMapping(typeName = "Query", fileName = "books")
    public DataFetcher getBooks() {
        return environment -> graphQLData.getBooks();
    }

    @FetcherMapping(typeName = "Mutation", fileName = "updateBook")
    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            List<Map<String, Object>> books = graphQLData.getBooks()
                .stream()
                .filter(book -> !Objects.equals(book.get("id"), newBook.get("id")))
                .collect(Collectors.toList());
            books.add(newBook);
            graphQLData.setBooks(books);
            return newBook;
        };
    }
}
