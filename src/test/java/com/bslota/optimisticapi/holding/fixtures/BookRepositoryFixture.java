package com.bslota.optimisticapi.holding.fixtures;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.Author;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookId;
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

    public AvailableBook availableBookInTheSystemWith(Version version) {
        AvailableBook availableBook = BookFixture.someAvailableBookWith(version);
        bookRepository.save(availableBook);
        return availableBook;
    }

    public AvailableBook bookWasModifiedInTheMeantime(BookId bookId) {
        AvailableBook book = (AvailableBook) bookRepository.findBy(bookId).get();
        AvailableBook updatedBook = new AvailableBook(book.id(), Author.named("The updated author"),
                book.title(), book.isbn(), book.version());
        bookRepository.save(updatedBook);
        return updatedBook;
    }
}
