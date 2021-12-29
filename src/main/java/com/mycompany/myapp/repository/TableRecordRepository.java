package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.TableRecord;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@Repository
public interface TableRecordRepository extends MongoRepository<TableRecord, String> {
    List<TableRecord> findAllByCreatedDateBetween(Instant startDate, Instant endDate);
}
