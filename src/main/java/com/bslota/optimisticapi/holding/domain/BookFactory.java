package com.bslota.optimisticapi.holding.domain;

import java.util.UUID;

public class BookFactory {

    public Book createAvailableBook(Author author, Title title, ISBN isbn) {
        return new AvailableBook(BookId.of(UUID.randomUUID()), author, title, isbn);
    }
}
