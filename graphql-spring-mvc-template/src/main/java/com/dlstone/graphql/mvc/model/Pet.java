package com.dlstone.graphql.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Pet {
    private String id;
    private String name;
}
