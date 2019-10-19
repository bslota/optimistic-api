package com.bslota.optimisticapi.holding.config;

import com.bslota.optimisticapi.holding.application.PlacingOnHold;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.query.FindingBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringConfiguration {

    private final BookRepository bookRepository;

    public SpringConfiguration(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Bean
    PlacingOnHold placingOnHold() {
        return new PlacingOnHold(bookRepository);
    }

    @Bean
    FindingBook findingBook() {
        return new FindingBook(bookRepository);
    }
}
