package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Device}.
 */
public interface DeviceService {
    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceDTO save(DeviceDTO deviceDTO);

    /**
     * Partially updates a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO);

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceDTO> findAll(Pageable pageable);

    List<DeviceDTO> findAllByActive(boolean active);

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceDTO> findOne(String id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    List<DeviceSessionDTO> devicesWithSessions();

    DeviceSessionDTO deviceWithSession(String deviceId);

    DeviceSessionDTO startSession(String deviceId, SessionStartDTO sessionStart);

    DeviceSessionDTO stopSession(String deviceId, SessionEndDTO sessionEndDto);

    DeviceSessionDTO addProductToDeviceSession(String deviceId, String productId);

    DeviceSessionDTO deleteProductFromDeviceSession(String deviceId, String productId);

    DeviceSessionDTO moveDevice(String moveFromDeviceId, String moveToDeviceId);

    DeviceSessionDTO moveDeviceMulti(String moveFromDeviceId, boolean multi);

    Session getDeviceActiveSession(String deviceId);
}
