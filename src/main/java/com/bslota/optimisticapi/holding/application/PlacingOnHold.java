package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.aggregate.StaleStateIdentified;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;

import java.util.Optional;

public class PlacingOnHold {

    private final BookRepository bookRepository;

    public PlacingOnHold(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Result handle(PlaceOnHoldCommand command) {
        Optional<Book> foundBook = bookRepository.findBy(command.getBookId());
        return foundBook
                .map(book -> handle(command, book))
                .orElseGet(BookNotFound::new);
    }

    private Result handle(PlaceOnHoldCommand command, Book book) {
        if (book instanceof AvailableBook) {
            return handle(command, (AvailableBook) book);
        } else {
            return new BookConflictIdentified(book);
        }
    }

    private Result handle(PlaceOnHoldCommand command, AvailableBook book) {
        try {
            PlacedOnHoldBook placedOnHoldBook =
                    book.placeOnHoldBy(command.getPatronId())
                            .withVersion(command.getVersion());
            bookRepository.save(placedOnHoldBook);
            return new BookPlacedOnHold();
        } catch (StaleStateIdentified ex) {
            Book currentState = bookRepository.findBy(book.id()).orElse(null);
            return new BookConflictIdentified(currentState);
        }
    }
}
