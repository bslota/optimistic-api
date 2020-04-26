package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.aggregate.Version;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.fixtures.BookRepositoryFixture;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static com.bslota.optimisticapi.holding.fixtures.BookFixture.someBookId;
import static com.bslota.optimisticapi.holding.fixtures.BookFixture.someVersion;
import static com.bslota.optimisticapi.holding.fixtures.PatronFixture.somePatronId;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BookAPITest {

    @Autowired
    private BookRepositoryFixture bookRepositoryFixture;

    @Autowired
    private BookAPIFixture api;

    private PatronId patronId = somePatronId();

    @Test
    public void shouldNotFindAnyBook() throws Exception {
        //given
        BookId idOfNonExistingBook = someBookId();

        //when
        ResultActions resultActions = api.getBookWith(idOfNonExistingBook);

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnExistingBook() throws Exception {
        //given
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystem();

        //when
        ResultActions resultActions = api.getBookWith(availableBook.id());

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
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystem();

        //when
        ResultActions resultActions = api.sendPlaceOnHoldCommandFor(availableBook.id(), patronId);

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBookOnHoldAfterItIsPlacedOnHold() throws Exception {
        //given
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystem();

        //and
        api.sendPlaceOnHoldCommandFor(availableBook.id(), patronId);

        //when
        ResultActions resultActions = api.getBookWith(availableBook.id());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availableBook.id().asString()))
                .andExpect(jsonPath("$.heldBy").value(patronId.asString()))
                .andExpect(jsonPath("$.status").value("PLACED_ON_HOLD"));
    }

    @Test
    public void shouldIncludeETagBasedOnVersionInBookViewResponse() throws Exception {
        //given
        Version version = someVersion();
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystemWith(version);

        //when
        ResultActions resultActions = api.getBookWith(availableBook.id());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string(ETAG, String.format("\"%d\"", version.asLong())))
                .andExpect(jsonPath("$.version").doesNotExist());
    }
}