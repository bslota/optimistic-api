package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.fixtures.BookRepositoryFixture;
import com.bslota.optimisticapi.holding.infrastructure.rest.BookAPIFixture.TestPlaceOnHoldCommand;
import com.bslota.optimisticapi.holding.query.BookView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static com.bslota.optimisticapi.holding.fixtures.BookFixture.bookIdFrom;
import static com.bslota.optimisticapi.holding.fixtures.PatronFixture.somePatronId;
import static com.bslota.optimisticapi.holding.infrastructure.rest.BookAPIFixture.TestPlaceOnHoldCommand.placeOnHoldCommandFor;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PlacingOnHoldConcurrencyTest {

    @Autowired
    private BookRepositoryFixture bookRepositoryFixture;

    @Autowired
    private BookAPIFixture api;

    @Test
    public void shouldSignalConflictWhenIfMatchHeaderIsMissing() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //and
        BookView book = api.viewBookWith(availableBook.id());

        //and
        AvailableBook updatedBook = bookWasModifiedInTheMeantime(bookIdFrom(book.getId()));

        //when Bruce places book on hold
        PatronId bruce = somePatronId();
        TestPlaceOnHoldCommand command = placeOnHoldCommandFor(book.getId(), bruce, book.getVersion()).withoutIfMatchHeader();
        ResultActions bruceResult = api.send(command);

        //then
        bruceResult
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.id").value(updatedBook.id().asString()))
                .andExpect(jsonPath("$.title").value(updatedBook.title().asString()))
                .andExpect(jsonPath("$.isbn").value(updatedBook.isbn().asString()))
                .andExpect(jsonPath("$.author").value(updatedBook.author().asString()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    public void shouldSignalPreconditionFailed() throws Exception {
        //given
        AvailableBook availableBook = availableBookInTheSystem();

        //and
        ResultActions bookViewResponse = api.getBookWith(availableBook.id());
        BookView book = api.parseBookViewFrom(bookViewResponse);
        String eTag = bookViewResponse.andReturn().getResponse().getHeader(ETAG);

        //and
        bookWasModifiedInTheMeantime(bookIdFrom(book.getId()));

        //when Bruce places book on hold
        PatronId bruce = somePatronId();
        TestPlaceOnHoldCommand command = placeOnHoldCommandFor(book.getId(), bruce, book.getVersion())
                .withIfMatchHeader(eTag);
        ResultActions bruceResult = api.send(command);

        //then
        bruceResult.andExpect(status().isPreconditionFailed());
    }

    private AvailableBook availableBookInTheSystem() {
        return bookRepositoryFixture.availableBookInTheSystem();
    }

    private AvailableBook bookWasModifiedInTheMeantime(BookId bookId) {
        return bookRepositoryFixture.bookWasModifiedInTheMeantime(bookId);
    }
}
