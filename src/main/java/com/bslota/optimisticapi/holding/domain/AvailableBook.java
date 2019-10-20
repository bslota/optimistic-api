package com.bslota.optimisticapi.holding.domain;

import com.bslota.optimisticapi.holding.aggregate.Version;

public final class AvailableBook implements Book {

    private final BookId id;
    private final Author author;
    private final Title title;
    private final ISBN isbn;
    private final Version version;

    public AvailableBook(BookId id, Author author, Title title, ISBN isbn, Version version) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.version = version;
    }

    public PlacedOnHoldBook placeOnHoldBy(PatronId patronId) {
        return new PlacedOnHoldBook(id, author, title, isbn, patronId, version);
    }

    @Override
    public BookId id() {
        return id;
    }

    @Override
    public Author author() {
        return author;
    }

    @Override
    public Title title() {
        return title;
    }

    @Override
    public ISBN isbn() {
        return isbn;
    }

    @Override
    public Version version() {
        return version;
    }
}
