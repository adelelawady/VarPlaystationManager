package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderHandlerDomain;
import com.mycompany.myapp.domain.Table;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Table entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderHandlerRepository extends MongoRepository<OrderHandlerDomain, String> {
    List<OrderHandlerDomain> findAllByTypeOrderByIndex(String type);
}
