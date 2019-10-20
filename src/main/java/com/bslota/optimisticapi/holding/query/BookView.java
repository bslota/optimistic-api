package com.bslota.optimisticapi.holding.query;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
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

    private BookView(String id, String author, String title, String isbn, String status, String patronId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.status = status;
        this.patronId = patronId;
    }

    static BookView from(Book book) {
        String patronId = (book instanceof PlacedOnHoldBook) ? ((PlacedOnHoldBook) book).getPatronId().asString() : null;
        return new BookView(book.id().asString(),
                book.author().asString(),
                book.title().asString(),
                book.isbn().asString(),
                statusOf(book),
                patronId);
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
