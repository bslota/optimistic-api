package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ETag wrapping class. Created according to @{link: https://tools.ietf.org/html/rfc7232#section-2.3}
 */
class ETag {

    private static final String EMPTY = "";
    private static final String WEAK_VALIDATION_FLAG = "W/";
    private static final String QUOTE = "\"";

    private final String value;

    private ETag(String value) {
        this.value = assemblyQuotedValue(value);
    }

    @JsonCreator
    static ETag of(String value) {
        return new ETag(value);
    }

    static ETag of(Version version) {
        return of(String.valueOf(version.asLong()));
    }

    @JsonValue
    String getValue() {
        return value;
    }

    String getTrimmedValue() {
        return value.replace(WEAK_VALIDATION_FLAG, EMPTY).replace(QUOTE, EMPTY);
    }

    private String assemblyQuotedValue(String value) {
        String quotedValue = value;
        if (!value.startsWith(QUOTE)) {
            quotedValue = QUOTE + quotedValue;
        }
        if (!value.endsWith(QUOTE)) {
            quotedValue = quotedValue + QUOTE;
        }
        return quotedValue;
    }
}
