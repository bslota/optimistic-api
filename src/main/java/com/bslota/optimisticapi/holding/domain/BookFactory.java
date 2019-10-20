package com.bslota.optimisticapi.holding.domain;

import com.bslota.optimisticapi.holding.aggregate.Version;

import java.util.UUID;

public class BookFactory {

    private static final Version INITIAL_VERSION = Version.from(0L);

    public Book createAvailableBook(Author author, Title title, ISBN isbn) {
        return new AvailableBook(BookId.of(UUID.randomUUID()), author, title, isbn, INITIAL_VERSION);
    }
}
