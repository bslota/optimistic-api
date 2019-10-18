package com.bslota.optimisticapi.holding.query;

import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;

import java.util.Optional;

public class FindingBook {

    private final BookRepository bookRepository;

    public FindingBook(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<BookView> findBy(BookId bookId) {
        return bookRepository.findBy(bookId).map(BookView::from);
    }
}
