package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Record;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
    @Query("{}")
    Page<Record> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Record> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Record> findOneWithEagerRelationships(String id);

    Page<Record> findAllByStartBetween(Pageable pageable, Instant startDate, Instant endDate);

    Page<Record> findAllByEndBetween(Pageable pageable, Instant startDate, Instant endDate);

    Page<Record> findAllByStartBetweenOrEndBetween(
        Pageable pageable,
        Instant startDate,
        Instant endDate,
        Instant startDatex,
        Instant endDatex
    );

    List<Record> findAllByStartBetween(Instant startDate, Instant endDate);

    List<Record> findAllByEndBetween(Instant startDate, Instant endDate);

    List<Record> findAllByStartBetweenOrEndBetween(Instant startDate, Instant endDate, Instant startDatex, Instant endDatex);
}
