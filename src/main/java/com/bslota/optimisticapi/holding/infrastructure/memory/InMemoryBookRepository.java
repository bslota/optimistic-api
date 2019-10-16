package com.bslota.optimisticapi.holding.infrastructure.memory;

import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookRepository implements BookRepository {

    private Map<BookId, Book> store = new HashMap<>();

    @Override
    public Optional<Book> findBy(BookId bookId) {
        return Optional.ofNullable(store.get(bookId));
    }

    @Override
    public void save(Book book) {
        store.put(book.id(), book);
    }
}
