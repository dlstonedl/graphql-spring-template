package com.dlstone.graphql.util.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoaderMapping {
    String name() default "";
}
