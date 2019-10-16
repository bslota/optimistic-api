package com.bslota.optimisticapi.holding.domain;

public interface Book {

    BookId id();

    Author author();

    Title title();

    ISBN isbn();
}
