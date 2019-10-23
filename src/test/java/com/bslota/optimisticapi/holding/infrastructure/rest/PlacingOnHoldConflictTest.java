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

import static com.bslota.optimisticapi.holding.fixtures.PatronFixture.somePatronId;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
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
        BookView book = api.getDeserializedBookWith(availableBook.id());

        //when Bruce places book on hold
        PatronId bruce = somePatronId();
        ResultActions bruceResult =  api.sendPlaceOnHoldCommandFor(book.getId(), bruce, book.getVersion());

        //then
        bruceResult.andExpect(status().isNoContent());

        //when Steve tries to place the same book on hold
        ResultActions steveResult =  api.sendPlaceOnHoldCommandFor(book.getId(), bruce, book.getVersion());

        //then
        steveResult.andExpect(status().isConflict());

    }
}
