package com.bslota.optimisticapi.holding.infrastructure.jpa;

import com.bslota.optimisticapi.holding.aggregate.StaleStateIdentified;
import com.bslota.optimisticapi.holding.domain.AvailableBook;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import com.bslota.optimisticapi.holding.domain.PatronId;
import com.bslota.optimisticapi.holding.domain.PlacedOnHoldBook;
import com.bslota.optimisticapi.holding.fixtures.BookRepositoryFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.bslota.optimisticapi.holding.fixtures.PatronFixture.somePatronId;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@RunWith(SpringRunner.class)
public class OptimisticLockingTest {

    @Autowired
    private BookRepositoryFixture bookRepositoryFixture;

    @Autowired
    private BookRepository bookRepository;

    private PatronId somePatronId = somePatronId();

    @Test(expected = StaleStateIdentified.class)
    public void savingEntityInCaseOfConflictShouldResultInError() {
        //given
        AvailableBook availableBook = bookRepositoryFixture.availableBookInTheSystem();

        //and
        AvailableBook loadedBook = (AvailableBook) bookRepository.findBy(availableBook.id()).get();
        PlacedOnHoldBook loadedBookPlacedOnHold = loadedBook.placeOnHoldBy(somePatronId);

        //and
        bookWasModifiedInTheMeantime(availableBook);

        //when
        bookRepository.save(loadedBookPlacedOnHold);

    }

    private void bookWasModifiedInTheMeantime(AvailableBook availableBook) {
        PatronId patronId = somePatronId();
        PlacedOnHoldBook placedOnHoldBook = availableBook.placeOnHoldBy(patronId);
        bookRepository.save(placedOnHoldBook);
    }

}