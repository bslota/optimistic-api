package com.bslota.optimisticapi.holding.fixtures;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookRepositoryFixture {

    @Autowired
    private BookRepository bookRepository;

    public AvailableBook availableBookInTheSystem() {
        AvailableBook availableBook = BookFixture.someAvailableBook();
        bookRepository.save(availableBook);
        return availableBook;
    }
}
