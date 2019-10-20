package com.bslota.optimisticapi.holding.domain;

import com.bslota.optimisticapi.holding.aggregate.Version;

public interface Book {

    BookId id();

    Author author();

    Title title();

    ISBN isbn();

    Version version();
}
