package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.application.BookConflictIdentified;
import com.bslota.optimisticapi.holding.application.BookNotFound;
import com.bslota.optimisticapi.holding.application.BookPlacedOnHold;
import com.bslota.optimisticapi.holding.application.PlaceOnHoldCommand;
import com.bslota.optimisticapi.holding.application.PlacingOnHold;
import com.bslota.optimisticapi.holding.application.Result;
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
class BookHoldingController {

    private final PlacingOnHold placingOnHold;

    BookHoldingController(PlacingOnHold placingOnHold) {
        this.placingOnHold = placingOnHold;
    }

    @PatchMapping("/{bookId}")
    ResponseEntity updateBookStatus(@PathVariable("bookId") UUID bookId,
                                    @RequestBody UpdateBookStatus command) {
        if (PLACED_ON_HOLD.equals(command.getStatus())) {
            Result result = placingOnHold.placeOnHold(new PlaceOnHoldCommand(BookId.of(bookId), command.patronId()));
            return buildResponseFrom(result);
        } else {
            return ResponseEntity.ok().build(); //we do not care about it now
        }
    }

    private ResponseEntity buildResponseFrom(Result result) {
        if (result instanceof BookPlacedOnHold) {
            return ResponseEntity.noContent().build();
        } else if (result instanceof BookNotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof BookConflictIdentified) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(((BookConflictIdentified) result).currentState().orElse(null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
