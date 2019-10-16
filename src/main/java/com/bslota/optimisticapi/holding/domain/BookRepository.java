package com.bslota.optimisticapi.holding.domain;

import java.util.Optional;

public interface BookRepository {

    Optional<Book> findBy(BookId bookId);

    void save(Book book);
}
