package com.bslota.optimisticapi.holding.domain;

import org.junit.Test;

import static com.bslota.optimisticapi.holding.domain.BookFixture.someAuthor;
import static com.bslota.optimisticapi.holding.domain.BookFixture.someISBN;
import static com.bslota.optimisticapi.holding.domain.BookFixture.someTitle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookFactoryTest {

    private BookFactory bookFactory = new BookFactory();
    private Author author = someAuthor();
    private Title title = someTitle();
    private ISBN isbn = someISBN();

    @Test
    public void shouldCreateBookWithProperTitle() {
        //when
        Book book = bookFactory.createAvailableBook(author, title, isbn);

        //then
        assertEquals(title, book.title());
    }

    @Test
    public void shouldCreateBookWithProperAuthor() {
        //when
        Book book = bookFactory.createAvailableBook(author, title, isbn);

        //then
        assertEquals(author, book.author());
    }

    @Test
    public void shouldCreateBookWithProperISBN() {
        //when
        Book book = bookFactory.createAvailableBook(author, title, isbn);

        //then
        assertEquals(isbn, book.isbn());
    }

    @Test
    public void shouldCreateBookWithID() {
        //when
        Book book = bookFactory.createAvailableBook(author, title, isbn);

        //then
        assertNotNull(book.id());
    }

    @Test
    public void shouldCreateAvailableBook() {
        //when
        Book book = bookFactory.createAvailableBook(author, title, isbn);

        //then
        assertTrue(book instanceof AvailableBook);
    }

}