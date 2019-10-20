package com.bslota.optimisticapi.holding.domain;

public final class AvailableBook implements Book {

    private final BookId id;
    private final Author author;
    private final Title title;
    private final ISBN isbn;

    public AvailableBook(BookId id, Author author, Title title, ISBN isbn) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
    }

    public PlacedOnHoldBook placeOnHoldBy(PatronId patronId) {
        return new PlacedOnHoldBook(id, author, title, isbn, patronId);
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
