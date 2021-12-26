package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Table entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableRepository extends MongoRepository<Table, String> {}
