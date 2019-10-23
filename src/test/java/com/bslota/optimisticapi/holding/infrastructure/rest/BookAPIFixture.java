package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.query.BookView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@Component
class BookAPIFixture {

    @Autowired(required = false)
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    BookView getDeserializedBookWith(BookId id) throws Exception {
        String content = getBookWith(id)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(content, BookView.class);
    }

    ResultActions getBookWith(BookId id) throws Exception {
        return mockMvc.perform(get("/books/{id}", id.asString()));
    }

    ResultActions sendPlaceOnHoldCommandFor(BookId id, PatronId patronId) throws Exception {
        return sendPlaceOnHoldCommandFor(id.asString(), patronId, 0L);
    }

    ResultActions sendPlaceOnHoldCommandFor(String bookId, PatronId patronId, long version) throws Exception {
        return mockMvc
                .perform(patch("/books/{id}", bookId)
                        .content(updateStatusBodyWith("PLACED_ON_HOLD", patronId, version))
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));
    }

    private String updateStatusBodyWith(String status, PatronId patronId, long version) {
        return String.format("{\"status\" : \"%s\", \"patronId\" : \"%s\", \"version\" : \"%d\"}",
                status, patronId.asString(), version);
    }

}
