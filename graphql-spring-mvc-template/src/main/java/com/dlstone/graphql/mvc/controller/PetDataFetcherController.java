package com.dlstone.graphql.mvc.controller;

import com.dlstone.graphql.mvc.repository.PetRepository;
import com.dlstone.graphql.util.annotation.GraphQLController;
import com.dlstone.graphql.util.annotation.FetcherMapping;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

@GraphQLController
public class PetDataFetcherController {

    private PetRepository petRepository;

    @Autowired
    public PetDataFetcherController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @FetcherMapping(typeName = "Query", fileName = "pets")
    public DataFetcher petsDataFetcher() {
        return environment -> petRepository.getPets();
    }
}
