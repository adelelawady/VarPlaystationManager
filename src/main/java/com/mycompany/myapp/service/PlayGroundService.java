package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Playground;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Device}.
 */
public interface PlayGroundService {
    /**
     *
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    Playground save(Playground deviceDTO);

    /**
     * Partially updates a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Playground> findAll(Pageable pageable);

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Playground> findOne(String id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    List<Playground> devicesWithSessions();

    Playground deviceWithSession(String deviceId);

    Playground startSession(String deviceId, SessionStartDTO sessionStart);

    Playground stopSession(String deviceId, SessionEndDTO sessionEndDto);

    Playground addProductToDeviceSession(String deviceId, String productId);

    Playground deleteProductFromDeviceSession(String deviceId, String productId);

    Session getDeviceActiveSession(String deviceId);
}
