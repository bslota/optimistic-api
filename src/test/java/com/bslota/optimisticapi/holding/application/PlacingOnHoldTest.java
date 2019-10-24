package com.bslota.optimisticapi.holding.application;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
import com.bslota.optimisticapi.holding.fixtures.BookFixture;
import com.bslota.optimisticapi.holding.infrastructure.memory.InMemoryBookRepository;
import org.junit.Test;

import static com.bslota.optimisticapi.holding.fixtures.BookFixture.someAvailableBook;
import static com.bslota.optimisticapi.holding.fixtures.BookFixture.someVersion;
import static com.bslota.optimisticapi.holding.fixtures.PatronFixture.somePatronId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlacingOnHoldTest {

    private BookRepository bookRepository = new InMemoryBookRepository();
    private PlacingOnHold placingOnHold = new PlacingOnHold(bookRepository);
    private PatronId patronId = somePatronId();

    @Test
    public void shouldPlaceOnHoldAvailableBook() {
        //given
        AvailableBook availableBook = someExistingAvailableBook();

        //when
        Result result = placingOnHold.handle(new PlaceOnHoldCommand(availableBook.id(), patronId, availableBook.version()));

        //then
        assertTrue(result instanceof BookPlacedOnHold);

        //and
        Book book = bookRepository.findBy(availableBook.id()).get();
        assertTrue(book instanceof PlacedOnHoldBook);
    }

    @Test
    public void bookPlacedOnHoldShouldHaveSameAttributesAsAvailableOne() {
        //given
        AvailableBook availableBook = someExistingAvailableBook();

        //when
        placingOnHold.handle(new PlaceOnHoldCommand(availableBook.id(), patronId, availableBook.version()));

        //then
        PlacedOnHoldBook book = (PlacedOnHoldBook) bookRepository.findBy(availableBook.id()).get();
        assertEquals(availableBook.id(), book.id());
        assertEquals(availableBook.author(), book.author());
        assertEquals(availableBook.title(), book.title());
        assertEquals(availableBook.isbn(), book.isbn());
        assertEquals(patronId, book.getPatronId());
    }

    @Test
    public void shouldReturnNotFoundResultWhenBookDoesNotExist() {
        //given
        BookId idOfNotExistingBook = BookFixture.someBookId();

        //when
        Result result = placingOnHold.handle(new PlaceOnHoldCommand(idOfNotExistingBook, patronId, someVersion()));

        //then
        assertTrue(result instanceof BookNotFound);
    }

    @Test
    public void shouldReturnConflictWhenBookIsNoLongerAvailable() {
        //given
        Book placedOnHoldBook = somePlacedOnHoldBook();

        //when
        Result result = placingOnHold.handle(new PlaceOnHoldCommand(placedOnHoldBook.id(), patronId, placedOnHoldBook.version()));

        //then
        assertTrue(result instanceof BookConflictIdentified);
    }

    private AvailableBook someExistingAvailableBook() {
        AvailableBook book = someAvailableBook();
        bookRepository.save(book);
        return book;
    }

    private PlacedOnHoldBook somePlacedOnHoldBook() {
        PlacedOnHoldBook book = someAvailableBook().placeOnHoldBy(patronId);
        bookRepository.save(book);
        return book;
    }
}