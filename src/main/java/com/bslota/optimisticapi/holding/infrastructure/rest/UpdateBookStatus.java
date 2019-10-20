package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.domain.Status;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

class UpdateBookStatus {

    private final Status status;
    private final PatronId patronId;

    @JsonCreator
    UpdateBookStatus(@JsonProperty("status") Status status,
                     @JsonProperty("patronId") UUID patronId) {
        this.status = status;
        this.patronId = PatronId.from(patronId);
    }

    Status getStatus() {
        return status;
    }

    PatronId patronId() {
        return patronId;
    }
}
