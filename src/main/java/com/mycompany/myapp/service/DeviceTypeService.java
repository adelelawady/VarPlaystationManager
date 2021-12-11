package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DeviceTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.DeviceType}.
 */
public interface DeviceTypeService {
    /**
     * Save a deviceType.
     *
     * @param deviceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceTypeDTO save(DeviceTypeDTO deviceTypeDTO);

    /**
     * Partially updates a deviceType.
     *
     * @param deviceTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceTypeDTO> partialUpdate(DeviceTypeDTO deviceTypeDTO);

    /**
     * Get all the deviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deviceType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceTypeDTO> findOne(String id);

    /**
     * Delete the "id" deviceType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
