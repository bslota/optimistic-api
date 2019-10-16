package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.application.PlacingOnHold;
import com.bslota.optimisticapi.holding.domain.BookId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.bslota.optimisticapi.holding.domain.Status.PLACED_ON_HOLD;

@RestController
@RequestMapping("/books")
class BookController {

    private final PlacingOnHold placingOnHold;

    BookController(PlacingOnHold placingOnHold) {
        this.placingOnHold = placingOnHold;
    }

    @PatchMapping("/{bookId}")
    ResponseEntity updateBookStatus(@PathVariable("bookId") UUID bookId, @RequestBody UpdateBookStatus command) {
        if (PLACED_ON_HOLD.equals(command.getStatus())) {
            placingOnHold.placeOnHold(BookId.of(bookId));
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }
}
