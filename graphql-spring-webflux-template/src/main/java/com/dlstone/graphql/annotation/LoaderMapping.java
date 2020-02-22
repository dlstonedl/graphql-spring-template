package com.dlstone.graphql.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoaderMapping {
    String name() default "";
}
