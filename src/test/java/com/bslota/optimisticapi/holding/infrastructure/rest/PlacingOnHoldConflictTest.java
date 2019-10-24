package com.bslota.optimisticapi.holding.infrastructure.rest;

import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.fixtures.BookRepositoryFixture;
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
import static org.hamcrest.Matchers.not;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PlacingOnHoldConflictTest {

    @Autowired
    private BookRepositoryFixture bookRepositoryFixture;

    @Autowired
    private BookAPIFixture api;

    @Test
    public void shouldSignalConflict() throws Exception {
        //given
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystem();

        //and
        BookView book = api.viewBookWith(availableBook.id());

        //and
        AvailableBook updatedBook = bookRepositoryFixture.bookWasModifiedInTheMeantime(bookIdFrom(book.getId()));

        //when Bruce places book on hold
        PatronId bruce = somePatronId();
        ResultActions bruceResult =  api.sendPlaceOnHoldCommandFor(book.getId(), bruce, book.getVersion());

        //then
        bruceResult
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.id").value(updatedBook.id().asString()))
                .andExpect(jsonPath("$.title").value(updatedBook.title().asString()))
                .andExpect(jsonPath("$.isbn").value(updatedBook.isbn().asString()))
                .andExpect(jsonPath("$.author").value(updatedBook.author().asString()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.version").value(not(updatedBook.version().asLong())));
    }
}
