package com.bslota.optimisticapi.holding.fixtures;

import com.bslota.optimisticapi.holding.domain.PatronId;

import java.util.UUID;

public class PatronFixture {

    public static PatronId somePatronId() {
        return PatronId.from(UUID.randomUUID());
    }
}
