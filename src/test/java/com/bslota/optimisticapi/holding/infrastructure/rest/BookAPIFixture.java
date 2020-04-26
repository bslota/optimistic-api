package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.query.BookView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.IF_MATCH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@Component
class BookAPIFixture {

    @Autowired(required = false)
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    BookView viewBookWith(BookId id) throws Exception {
        return parseBookViewFrom(getBookWith(id));
    }

    BookView parseBookViewFrom(ResultActions resultActions) throws Exception {
        String content = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(content, BookView.class);
    }

    ResultActions getBookWith(BookId id) throws Exception {
        return mockMvc.perform(get("/books/{id}", id.asString()));
    }

    ResultActions sendPlaceOnHoldCommandFor(BookId id, PatronId patronId) throws Exception {
        return send(TestPlaceOnHoldCommand.placeOnHoldCommandFor(id.asString(), patronId, 0L));
    }

    ResultActions send(TestPlaceOnHoldCommand command) throws Exception {
        HttpHeaders httpHeaders = httpHeaders();
        command.ifMatchHeader().ifPresent(ifMatch -> httpHeaders.put(IF_MATCH, Collections.singletonList(ifMatch)));

        return mockMvc
                .perform(patch("/books/{id}", command.bookId())
                        .content(command.body())
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(CONTENT_TYPE, Collections.singletonList(APPLICATION_JSON_VALUE));
        return httpHeaders;
    }

    static class TestPlaceOnHoldCommand {
        private String bookId;
        private String patronId;
        private long version;
        private String ifMatchHeader;

        TestPlaceOnHoldCommand(String bookId, String patronId, long version) {
            this.bookId = bookId;
            this.patronId = patronId;
            this.version = version;
            this.ifMatchHeader = format("\"%d\"", version);
        }

        static TestPlaceOnHoldCommand placeOnHoldCommandFor(BookView bookView, PatronId patronId) {
            return placeOnHoldCommandFor(bookView.getId(), patronId, bookView.getVersion());
        }

        static TestPlaceOnHoldCommand placeOnHoldCommandFor(String bookId, PatronId patronId, long version) {
            return new TestPlaceOnHoldCommand(bookId, patronId.asString(), version);
        }

        TestPlaceOnHoldCommand withoutIfMatchHeader() {
            this.ifMatchHeader = null;
            return this;
        }

        TestPlaceOnHoldCommand withIfMatchHeader(String eTag) {
            this.ifMatchHeader = eTag;
            return this;
        }

        String bookId() {
            return bookId;
        }

        Optional<String> ifMatchHeader() {
            return Optional.ofNullable(ifMatchHeader);
        }

        String body() {
            return format("{\"status\" : \"%s\", \"patronId\" : \"%s\", \"version\" : \"%d\"}",
                    "PLACED_ON_HOLD", patronId, version);
        }
    }
}
