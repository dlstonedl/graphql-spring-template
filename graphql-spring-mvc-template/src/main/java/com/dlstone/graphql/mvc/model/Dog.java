package com.dlstone.graphql.mvc.model;

import lombok.Getter;

@Getter
public class Dog extends Pet {
    private String sex;

    public Dog(String id, String name, String sex) {
        super(id, name);
        this.sex = sex;
    }
}
