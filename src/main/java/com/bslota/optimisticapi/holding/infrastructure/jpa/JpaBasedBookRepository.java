package com.bslota.optimisticapi.holding.infrastructure.jpa;

import com.bslota.optimisticapi.holding.domain.Book;
import com.bslota.optimisticapi.holding.domain.BookId;
import com.bslota.optimisticapi.holding.domain.BookRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class JpaBasedBookRepository implements BookRepository {

    private final JpaBookRepository jpaBookRepository;

    JpaBasedBookRepository(JpaBookRepository jpaBookRepository) {
        this.jpaBookRepository = jpaBookRepository;
    }

    @Override
    public Optional<Book> findBy(BookId bookId) {
        return jpaBookRepository.findById(bookId.getValue())
                .map(BookEntity::toDomainModel);
    }

    @Override
    public void save(Book book) {
        BookEntity entity = BookEntity.from(book);
        jpaBookRepository.save(entity);
    }
}

interface JpaBookRepository extends Repository<BookEntity, UUID> {

    Optional<BookEntity> findById(UUID id);

    void save(BookEntity bookEntity);
}
