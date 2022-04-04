package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Playground;
import com.mycompany.myapp.domain.PlaygroundRecord;
import com.mycompany.myapp.domain.Record;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaygroundRecoredRepository extends MongoRepository<PlaygroundRecord, String> {
    List<PlaygroundRecord> findAllByStartBetween(Instant startDate, Instant endDate);
}
