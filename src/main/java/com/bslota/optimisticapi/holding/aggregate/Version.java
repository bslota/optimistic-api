package com.bslota.optimisticapi.holding.aggregate;

public class Version {

    private final long value;

    private Version(long value) {
        this.value = value;
    }

    public static Version from(long value) {
        return new Version(value);
    }

    public long asLong() {
        return value;
    }
}
