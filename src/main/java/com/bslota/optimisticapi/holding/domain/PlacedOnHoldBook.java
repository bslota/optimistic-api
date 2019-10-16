package com.bslota.optimisticapi.holding.domain;

public final class PlacedOnHoldBook implements Book {

    private final BookId id;
    private final Author author;
    private final Title title;
    private final ISBN isbn;

    PlacedOnHoldBook(BookId id, Author author, Title title, ISBN isbn) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
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
}
