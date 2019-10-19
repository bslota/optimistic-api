package com.bslota.optimisticapi.holding.infrastructure.jpa;

import com.bslota.optimisticapi.holding.domain.Author;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.ISBN;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
import com.bslota.optimisticapi.holding.domain.Title;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "book")
class BookEntity {

    @Id
    private UUID id;

    private String title;

    private String author;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookEntityStatus status;


    private BookEntity() {
    }

    private BookEntity(UUID id, String title, String author, String isbn, BookEntityStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }

    static BookEntity from(Book book) {
        return new BookEntity(book.id().getValue(),
                book.title().asString(),
                book.author().asString(),
                book.isbn().asString(),
                BookEntityStatus.of(book));
    }

    UUID getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getIsbn() {
        return isbn;
    }

    BookEntityStatus getStatus() {
        return status;
    }

    Book toDomainModel() {
        switch (status) {
            case AVAILABLE:
                return new AvailableBook(BookId.of(id), Author.named(author), Title.of(title), ISBN.of(isbn));
            case PLACED_ON_HOLD:
                return new PlacedOnHoldBook(BookId.of(id), Author.named(author), Title.of(title), ISBN.of(isbn));
            default:
                throw new IllegalStateException("Book state is invalid");
        }
    }
}