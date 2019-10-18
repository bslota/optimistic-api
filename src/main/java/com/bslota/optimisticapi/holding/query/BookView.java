package com.bslota.optimisticapi.holding.query;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;

public class BookView {

    private final String id;
    private final String author;
    private final String title;
    private final String isbn;
    private final String status;

    private BookView(String id, String author, String title, String isbn, String status) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.status = status;
    }

    static BookView from(Book book) {
        return new BookView(book.id().asString(),
                book.author().asString(),
                book.title().asString(),
                book.isbn().asString(),
                statusOf(book));
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
