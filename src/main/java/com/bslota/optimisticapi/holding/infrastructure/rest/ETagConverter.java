package com.bslota.optimisticapi.holding.infrastructure.rest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class ETagConverter implements Converter<String, ETag> {

    @Override
    public ETag convert(String source) {
        return ETag.of(source);
    }
}
