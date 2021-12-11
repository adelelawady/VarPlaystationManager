package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.RecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Record}.
 */
public interface RecordService {
    /**
     * Save a record.
     *
     * @param recordDTO the entity to save.
     * @return the persisted entity.
     */
    RecordDTO save(RecordDTO recordDTO);

    com.mycompany.myapp.domain.Record save(com.mycompany.myapp.domain.Record record);

    /**
     * Partially updates a record.
     *
     * @param recordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecordDTO> partialUpdate(RecordDTO recordDTO);

    /**
     * Get all the records.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecordDTO> findAll(Pageable pageable);

    /**
     * Get all the records with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecordDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" record.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecordDTO> findOne(String id);

    /**
     * Delete the "id" record.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
