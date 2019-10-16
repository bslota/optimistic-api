package com.bslota.optimisticapi.holding.config;

import com.bslota.optimisticapi.holding.application.PlacingOnHold;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.infrastructure.memory.InMemoryBookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringConfiguration {

    @Bean
    BookRepository inMemoryBookRepository() {
        return new InMemoryBookRepository();
    }

    @Bean
    PlacingOnHold placingOnHold() {
        return new PlacingOnHold(inMemoryBookRepository());
    }
}
