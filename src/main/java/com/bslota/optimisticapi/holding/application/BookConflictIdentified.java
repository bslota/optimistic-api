package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.domain.Book;

import java.util.Optional;

public final class BookConflictIdentified implements Result {

    private final Book currentState;

    BookConflictIdentified(Book currentState) {
        this.currentState = currentState;
    }

    public final Optional<Book> currentState() {
        return Optional.ofNullable(currentState);
    }
}
