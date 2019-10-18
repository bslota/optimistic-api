package com.bslota.optimisticapi.holding.domain;

import java.util.Objects;

public final class ISBN {

    private final String value;

    private ISBN(String value) {
        this.value = value;
    }

    public static ISBN of(String value) {
        return new ISBN(value);
    }

    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISBN isbn = (ISBN) o;
        return Objects.equals(value, isbn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
