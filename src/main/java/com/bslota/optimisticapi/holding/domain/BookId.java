package com.bslota.optimisticapi.holding.domain;

import java.util.Objects;
import java.util.UUID;

public final class BookId {

    private final UUID value;

    private BookId(UUID value) {
        this.value = value;
    }

    public static BookId of(UUID value) {
        return new BookId(value);
    }

    public String asString() {
        return value.toString();
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId = (BookId) o;
        return Objects.equals(value, bookId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
