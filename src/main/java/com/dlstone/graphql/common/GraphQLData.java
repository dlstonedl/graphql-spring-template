package com.dlstone.graphql.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
@Component
public class GraphQLData {
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

    public List<Map<String, String>> getBatchAuthors(List<String> authorIds) {
        log.info("authorIds: " + authorIds.toString());

        return authors
            .stream()
            .filter(author -> authorIds.contains(author.get("id")))
            .collect(Collectors.toList());
    }

}
