package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DeviceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTypeRepository extends MongoRepository<DeviceType, String> {}
