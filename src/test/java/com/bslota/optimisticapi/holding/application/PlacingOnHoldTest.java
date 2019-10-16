package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
import com.bslota.optimisticapi.holding.infrastructure.memory.InMemoryBookRepository;
import org.junit.Test;

import static com.bslota.optimisticapi.holding.fixtures.BookFixture.someAvailableBook;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlacingOnHoldTest {

    private BookRepository bookRepository = new InMemoryBookRepository();
    private PlacingOnHold placingOnHold = new PlacingOnHold(bookRepository);

    @Test
    public void shouldPlaceOnHoldAvailableBook() {
        //given
        AvailableBook availableBook = someExistingAvailableBook();

        //when
        placingOnHold.placeOnHold(availableBook.id());

        //then
        Book book = bookRepository.findBy(availableBook.id()).get();
        assertTrue(book instanceof PlacedOnHoldBook);
    }

    @Test
    public void bookPlacedOnHoldShouldHaveSameAttributesAsAvailableOne() {
        //given
        AvailableBook availableBook = someExistingAvailableBook();

        //when
        placingOnHold.placeOnHold(availableBook.id());

        //then
        PlacedOnHoldBook book = (PlacedOnHoldBook) bookRepository.findBy(availableBook.id()).get();
        assertEquals(availableBook.id(), book.id());
        assertEquals(availableBook.author(), book.author());
        assertEquals(availableBook.title(), book.title());
        assertEquals(availableBook.isbn(), book.isbn());
    }

    private AvailableBook someExistingAvailableBook() {
        AvailableBook book = someAvailableBook();
        bookRepository.save(book);
        return book;
    }
}