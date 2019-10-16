package com.bslota.optimisticapi.holding.domain;

public class BookFixture {

    private static BookFactory bookFactory = new BookFactory();

    public static AvailableBook someAvailableBook() {
        return (AvailableBook) bookFactory.createAvailableBook(someAuthor(), someTitle(), someISBN());
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
}
