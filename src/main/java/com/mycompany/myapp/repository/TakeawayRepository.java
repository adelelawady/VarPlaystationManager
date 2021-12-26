package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Takeaway;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Takeaway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TakeawayRepository extends MongoRepository<Takeaway, String> {}
