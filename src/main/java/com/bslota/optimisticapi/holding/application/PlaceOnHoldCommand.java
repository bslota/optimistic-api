package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;

public class PlaceOnHoldCommand {

    private final BookId bookId;
    private final PatronId patronId;
    private final Version version;

    public PlaceOnHoldCommand(BookId bookId, PatronId patronId, Version version) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.version = version;
    }

    BookId getBookId() {
        return bookId;
    }

    PatronId getPatronId() {
        return patronId;
    }

    public Version getVersion() {
        return version;
    }
}
