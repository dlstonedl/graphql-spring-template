package com.dlstone.graphql.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphQLDataFetchers {
    private static List<Map<String, Object>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorIds", Lists.newArrayList("author-1", "author-2", "author-3")),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorIds", Lists.newArrayList("author-2", "author-3")),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorIds", Lists.newArrayList("author-1", "author-3"))
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
                    .filter(book -> Objects.equals(book.get("id"), id))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getBooks() {
        return environment -> books;
    }

    public DataFetcher getAuthorDataFetcher() {
        return environment -> {
            Map<String,Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            log.info("authorIds: " + authorIds.toString());

            return authors
                    .stream()
                    .filter(author -> authorIds.contains(author.get("id")))
                    .collect(Collectors.toList());
        };
    }

    public DataFetcher getAuthorLoaderDataFetcher() {
        return environment -> {
            Map<String, Object> book = environment.getSource();
            ArrayList<String> authorIds = (ArrayList<String>) book.get("authorIds");
            DataLoader<String, Map<String, String>> authorDataLoader = environment.getDataLoader(GraphQLDataLoader.AUTHOR_LOADER);
            return authorDataLoader.loadMany(authorIds);
        };
    }

    public List<Map<String, String>> getBatchAuthors(List<String> authorIds) {
        log.info("authorIds: " + authorIds.toString());

        return authors
            .stream()
            .filter(author -> authorIds.contains(author.get("id")))
            .collect(Collectors.toList());
    }

    public DataFetcher updateBookDataFetcher() {
        return environment -> {
            Map<String, Object> newBook = environment.getArgument("bookInput");
            books = books
                .stream()
                .filter(book -> !Objects.equals(book.get("id"), newBook.get("id")))
                .collect(Collectors.toList());
            books.add(newBook);
            return newBook;
        };
    }

}
