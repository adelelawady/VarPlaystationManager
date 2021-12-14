package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.service.dto.SessionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Session}.
 */
public interface SessionService {
    /**
     * Save a session.
     *
     * @param sessionDTO the entity to save.
     * @return the persisted entity.
     */
    SessionDTO save(SessionDTO sessionDTO);

    Session save(Session session);
    /**
     * Partially updates a session.
     *
     * @param sessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionDTO> partialUpdate(SessionDTO sessionDTO);

    /**
     * Get all the sessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SessionDTO> findAll(Pageable pageable);

    /**
     * Get all the sessions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SessionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" session.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionDTO> findOne(String id);

    /**
     * Delete the "id" session.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    Session getDeviceActiveSession(String deviceId);

    void stopAllDeviceActiveSessions(String deviceId);

    void calculateDeviceSessionOrderesPrice(Session session);

    void addProductOrderToDeviceSession(Session session, Product product);
}
