package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ShopsOrders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ShopsOrders entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopsOrdersRepository extends MongoRepository<ShopsOrders, String> {}
