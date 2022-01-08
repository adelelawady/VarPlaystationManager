package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Session entity.
 */
@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    @Query("{}")
    Page<Session> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Session> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Session> findOneWithEagerRelationships(String id);

    List<Session> findByDeviceIdAndActive(String deviceId, boolean active);

    List<Session> findAllByActive(boolean active);
}
