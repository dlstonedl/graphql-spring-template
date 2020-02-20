package com.dlstone.graphql.common;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraphQLDataFetchers {
    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );

    public DataFetcher getBookByIdDataFetcher() {
        return environment -> {
            String id = environment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> StringUtils.equals(book.get("id"), id))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return environment -> {
            Map<String,String> book = environment.getSource();
            return authors
                    .stream()
                    .filter(author -> StringUtils.equals(author.get("id"), book.get("authorId")))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, String> newBook = environment.getArgument("bookInput");
            books = books
                .stream()
                .filter(book -> !StringUtils.equals(book.get("id"), newBook.get("id")))
                .collect(Collectors.toList());
            books.add(newBook);
            return newBook;
        };
    }

}
