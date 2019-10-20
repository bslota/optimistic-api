package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;

public class PlaceOnHoldCommand {

    private final BookId bookId;
    private final PatronId patronId;

    public PlaceOnHoldCommand(BookId bookId, PatronId patronId) {
        this.bookId = bookId;
        this.patronId = patronId;
    }

    BookId getBookId() {
        return bookId;
    }

    PatronId getPatronId() {
        return patronId;
    }
}
