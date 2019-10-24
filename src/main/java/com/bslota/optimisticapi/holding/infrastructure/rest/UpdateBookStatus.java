package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.domain.Status;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

class UpdateBookStatus {

    private final Status status;
    private final PatronId patronId;
    private final Version version;

    @JsonCreator
    UpdateBookStatus(@JsonProperty("status") Status status,
                     @JsonProperty("patronId") UUID patronId,
                     @JsonProperty("version") long version) {
        this.status = status;
        this.patronId = PatronId.from(patronId);
        this.version = Version.from(version);
    }

    Status getStatus() {
        return status;
    }

    PatronId patronId() {
        return patronId;
    }

    Version version() {
        return version;
    }
}
