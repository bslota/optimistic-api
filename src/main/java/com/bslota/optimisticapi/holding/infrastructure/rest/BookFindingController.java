package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.query.BookView;
import com.bslota.optimisticapi.holding.query.FindingBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/books")
class BookFindingController {

    private final FindingBook findingBook;

    public BookFindingController(FindingBook findingBook) {
        this.findingBook = findingBook;
    }

    @GetMapping("/{bookId}")
    ResponseEntity findBookWith(@PathVariable("bookId") UUID bookIdValue) {
        Optional<BookView> book = findingBook.findBy(BookId.of(bookIdValue));
        if (book.isPresent()) {
            return ResponseEntity.ok().body(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
