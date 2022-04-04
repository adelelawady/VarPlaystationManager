package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Playground;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaygroundRepository extends MongoRepository<Playground, String> {}
