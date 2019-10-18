package com.bslota.optimisticapi.holding.domain;

import java.util.Objects;

public final class Title {

    private final String title;

    private Title(String title) {
        this.title = title;
    }

    public static Title of(String value) {
        return new Title(value);
    }

    public String asString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
