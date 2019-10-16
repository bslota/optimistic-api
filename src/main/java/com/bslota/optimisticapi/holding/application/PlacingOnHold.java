package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;

public class PlacingOnHold {

    private final BookRepository bookRepository;

    public PlacingOnHold(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void placeOnHold(BookId id) {
        PlacedOnHoldBook placedOnHoldBook =
                bookRepository.findBy(id)
                        .filter(book -> book instanceof AvailableBook)
                        .map(book -> (AvailableBook) book)
                        .orElseThrow(() -> new IllegalStateException("Book does not exist or is not available"))
                        .placeOnHold();
        bookRepository.save(placedOnHoldBook);
    }
}
