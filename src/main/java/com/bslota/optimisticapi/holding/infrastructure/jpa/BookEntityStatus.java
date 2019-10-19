package com.bslota.optimisticapi.holding.infrastructure.jpa;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.Book;

enum BookEntityStatus {
    AVAILABLE, PLACED_ON_HOLD;

    static BookEntityStatus of(Book book) {
        if (book instanceof AvailableBook) {
            return AVAILABLE;
        } else
            return PLACED_ON_HOLD;
    }
}
