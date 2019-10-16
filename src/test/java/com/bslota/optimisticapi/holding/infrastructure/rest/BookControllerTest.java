package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.fixtures.BookFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldReturnNoContentWhenPlacingAvailableBookOnHold() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //when
        ResultActions resultActions = sendPlaceOnHoldCommandFor(availableBook.id());

        //then
        resultActions.andExpect(status().isNoContent());
    }

    private ResultActions sendPlaceOnHoldCommandFor(BookId id) throws Exception {
        return mockMvc
                .perform(patch("/books/{id}", id.asString())
                        .content("{\"status\" : \"PLACED_ON_HOLD\"}")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    private AvailableBook availableBookInTheSystem() {
        AvailableBook availableBook = BookFixture.someAvailableBook();
        bookRepository.save(availableBook);
        return availableBook;
    }
}