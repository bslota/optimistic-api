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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BookAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void shouldNotFindAnyBook() throws Exception {
        //given
        BookId idOfNonExistingBook = someBookId();

        //when
        ResultActions resultActions = getBookWith(idOfNonExistingBook);

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnExistingBook() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //when
        ResultActions resultActions = getBookWith(availableBook.id());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availableBook.id().asString()))
                .andExpect(jsonPath("$.title").value(availableBook.title().asString()))
                .andExpect(jsonPath("$.isbn").value(availableBook.isbn().asString()))
                .andExpect(jsonPath("$.author").value(availableBook.author().asString()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    public void shouldReturnNoContentWhenPlacingAvailableBookOnHold() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //when
        ResultActions resultActions = sendPlaceOnHoldCommandFor(availableBook.id());

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBookOnHoldAfterItIsPlacedOnHold() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //and
        sendPlaceOnHoldCommandFor(availableBook.id());

        //when
        ResultActions resultActions = getBookWith(availableBook.id());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availableBook.id().asString()))
                .andExpect(jsonPath("$.status").value("PLACED_ON_HOLD"));

    }

    private ResultActions sendPlaceOnHoldCommandFor(BookId id) throws Exception {
        return mockMvc
                .perform(patch("/books/{id}", id.asString())
                        .content("{\"status\" : \"PLACED_ON_HOLD\"}")
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));
    }

    private ResultActions getBookWith(BookId id) throws Exception {
        return mockMvc.perform(get("/books/{id}", id.asString()));
    }

    private AvailableBook availableBookInTheSystem() {
        AvailableBook availableBook = BookFixture.someAvailableBook();
        bookRepository.save(availableBook);
        return availableBook;
    }

    private BookId someBookId() {
        return BookId.of(UUID.randomUUID());
    }
}