package com.dlstone.graphql.mvc.controller;

import com.dlstone.graphql.mvc.model.Cat;
import com.dlstone.graphql.util.annotation.TypeController;
import com.dlstone.graphql.util.annotation.TypeMapping;
import graphql.schema.TypeResolver;

@TypeController
public class GraphQLTypeController {

    @TypeMapping(typeName = "Pet")
    public TypeResolver petTypeResolver() {
        return env -> {
            Object javaObject = env.getObject();
            return javaObject instanceof Cat ?
                env.getSchema().getObjectType("Cat") :
                env.getSchema().getObjectType("Dog");
        };
    }
}
