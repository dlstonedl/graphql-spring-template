package com.dlstone.graphql.mvc.model;

import lombok.Getter;

@Getter
public class Cat extends Pet {
    private String color;

    public Cat(String id, String name, String color) {
        super(id, name);
        this.color = color;
    }
}
