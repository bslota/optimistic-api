package com.bslota.optimisticapi.holding.domain;

import java.util.Objects;

public final class Author {

    private final String name;

    private Author(String name) {
        this.name = name;
    }

    public static Author named(String name) {
        return new Author(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
