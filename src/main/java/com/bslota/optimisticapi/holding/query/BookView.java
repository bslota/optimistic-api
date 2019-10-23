package com.bslota.optimisticapi.holding.query;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookView {

    private final String id;
    private final String author;
    private final String title;
    private final String isbn;
    @JsonProperty("heldBy")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String patronId;
    private final String status;
    private final long version;

    @JsonCreator
    private BookView(@JsonProperty("id") String id,
                     @JsonProperty("author") String author,
                     @JsonProperty("title") String title,
                     @JsonProperty("isbn") String isbn,
                     @JsonProperty("status") String status,
                     @JsonProperty("heldBy") String patronId,
                     @JsonProperty("version") long version) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.status = status;
        this.patronId = patronId;
        this.version = version;
    }

    static BookView from(Book book) {
        String patronId = (book instanceof PlacedOnHoldBook) ? ((PlacedOnHoldBook) book).getPatronId().asString() : null;
        return new BookView(book.id().asString(),
                book.author().asString(),
                book.title().asString(),
                book.isbn().asString(),
                statusOf(book),
                patronId,
                book.version().asLong());
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getStatus() {
        return status;
    }

    public long getVersion() {
        return version;
    }

    private static String statusOf(Book book) {
        if (book instanceof AvailableBook) {
            return "AVAILABLE";
        } else if (book instanceof PlacedOnHoldBook) {
            return "PLACED_ON_HOLD";
        } else {
            return "UNKNOWN";
        }
    }
}
