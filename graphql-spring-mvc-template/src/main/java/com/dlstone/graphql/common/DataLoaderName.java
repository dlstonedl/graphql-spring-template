package com.dlstone.graphql.common;

import lombok.Getter;

public enum DataLoaderName {
    AUTHOR_LOADER("author-loader");

    @Getter
    private String value;

    DataLoaderName(String value) {
        this.value = value;
    }
}
