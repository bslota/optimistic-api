package com.bslota.optimisticapi.holding.domain;

public final class PlacedOnHoldBook implements Book {

    private final BookId id;
    private final Author author;
    private final Title title;
    private final ISBN isbn;
    private final PatronId patronId;

    public PlacedOnHoldBook(BookId id, Author author, Title title, ISBN isbn, PatronId patronId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.patronId = patronId;
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

    public PatronId getPatronId() {
        return patronId;
    }
}
