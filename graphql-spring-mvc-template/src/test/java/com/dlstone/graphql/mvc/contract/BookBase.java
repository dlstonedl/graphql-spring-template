package com.dlstone.graphql.mvc.contract;

import com.dlstone.graphql.mvc.controller.GraphQLController;
import com.dlstone.graphql.util.common.GraphQLClient;
import com.google.common.collect.Maps;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookBase {

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(getGraphQLController());
    }

    private GraphQLController getGraphQLController() {
        GraphQLClient graphQLClient = mock(GraphQLClient.class);
        when(graphQLClient.invoke(any())).thenReturn(getGraphQlResult());
        return new GraphQLController(graphQLClient);
    }

    private CompletableFuture<Map<String, Object>> getGraphQlResult() {
        Map<String, Object> book = new HashMap<>();
        book.put("id", "book-1");
        book.put("name", "book-name");
        book.put("pageCount", 223);
        Map<String, String> author = Maps.newHashMap();
        author.put("firstName", "Joanne");
        author.put("lastName", "Rowling");
        book.put("authors", Arrays.asList(author));

        Map<String, Object> bookById = Maps.newHashMap();
        bookById.put("bookById", book);

        Map<String, Object> data = Maps.newHashMap();
        data.put("data", bookById);

        return CompletableFuture.completedFuture(data);
    }
}
