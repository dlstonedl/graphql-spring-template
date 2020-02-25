package com.dlstone.graphql.mvc.repository;

import com.dlstone.graphql.mvc.model.Cat;
import com.dlstone.graphql.mvc.model.Dog;
import com.dlstone.graphql.mvc.model.Pet;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetRepository {

    @Getter
    private List<Pet> pets = Lists.newArrayList(
        new Cat("catId1", "catName", "orange"),
        new Dog("dogId1", "dogName", "male")
    );

}
