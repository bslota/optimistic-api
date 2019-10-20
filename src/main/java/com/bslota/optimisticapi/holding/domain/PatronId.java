package com.bslota.optimisticapi.holding.domain;

import java.util.Objects;
import java.util.UUID;

public class PatronId {

    private final UUID value;

    private PatronId(UUID value) {
        this.value = value;
    }

    public static PatronId from(UUID value) {
        return new PatronId(value);
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
        PatronId patronId = (PatronId) o;
        return Objects.equals(value, patronId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
