package com.bslota.optimisticapi.holding.aggregate;

import java.util.UUID;

public class StaleStateIdentified extends RuntimeException {

    private StaleStateIdentified(UUID id) {
        super(String.format("Aggregate of id %s is stale", id));
    }

    public static StaleStateIdentified forAggregateWith(UUID id) {
        return new StaleStateIdentified(id);
    }
}
