package com.dlstone.graphql.mvc.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class GraphQLRepository {
    private List<Map<String, Object>> books = Arrays.asList(
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

    private List<Map<String, String>> authors = Arrays.asList(
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

    public List<Map<String, String>> getAuthorsByIds(List<String> authorIds) {
        log.info("authorIds: " + authorIds.toString());

        return authors
            .stream()
            .filter(author -> authorIds.contains(author.get("id")))
            .collect(Collectors.toList());
    }

    public Map<String, Object> getBookById(String id) {
        return books
            .stream()
            .filter(book -> Objects.equals(book.get("id"), id))
            .findFirst()
            .orElse(null);
    }

    public List<Map<String, Object>> getAllBooks() {
        return books;
    }

    public Map<String,Object> updateBook(Map<String, Object> newBook) {
        Stream<Map<String, Object>> bookStream = books
            .stream()
            .filter(book -> !Objects.equals(book.get("id"), newBook.get("id")));
        books = Stream.concat(bookStream, Stream.of(newBook)).collect(Collectors.toList());
        return newBook;
    }


}
