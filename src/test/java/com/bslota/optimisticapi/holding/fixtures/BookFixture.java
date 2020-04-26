package com.bslota.optimisticapi.holding.fixtures;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.Author;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookFactory;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.ISBN;
import com.bslota.optimisticapi.holding.domain.Title;

import java.util.Random;
import java.util.UUID;

import static java.lang.Math.abs;

public class BookFixture {

    private static BookFactory bookFactory = new BookFactory();

    public static AvailableBook someAvailableBook() {
        return (AvailableBook) bookFactory.createAvailableBook(someAuthor(), someTitle(), someISBN());
    }

    public static AvailableBook someAvailableBookWith(Version version) {
        return new AvailableBook(BookId.of(UUID.randomUUID()), someAuthor(), someTitle(), someISBN(), version);
    }

    public static BookId bookIdFrom(String value) {
        return BookId.of(UUID.fromString(value));
    }

    public static BookId someBookId() {
        return BookId.of(UUID.randomUUID());
    }

    public static ISBN someISBN() {
        return ISBN.of("054792822X");
    }

    public static Title someTitle() {
        return Title.of("The Hobbit, or There and Back Again");
    }

    public static Author someAuthor() {
        return Author.named("John R. R. Tolkien");
    }

    public static Version someVersion() {
        return Version.from(abs(new Random().nextLong()));
    }
}
