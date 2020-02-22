package com.dlstone.graphql.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FetcherMapping {
    String typeName() default "";
    String fileName() default "";
}
