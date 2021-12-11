package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Record;
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
}
