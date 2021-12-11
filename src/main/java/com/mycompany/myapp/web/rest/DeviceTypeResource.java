package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DeviceTypeRepository;
import com.mycompany.myapp.service.DeviceTypeService;
import com.mycompany.myapp.service.dto.DeviceTypeDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DeviceType}.
 */
@RestController
@RequestMapping("/api")
public class DeviceTypeResource {

    private final Logger log = LoggerFactory.getLogger(DeviceTypeResource.class);

    private static final String ENTITY_NAME = "deviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceTypeService deviceTypeService;

    private final DeviceTypeRepository deviceTypeRepository;

    public DeviceTypeResource(DeviceTypeService deviceTypeService, DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeService = deviceTypeService;
        this.deviceTypeRepository = deviceTypeRepository;
    }

    /**
     * {@code POST  /device-types} : Create a new deviceType.
     *
     * @param deviceTypeDTO the deviceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceTypeDTO, or with status {@code 400 (Bad Request)} if the deviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-types")
    public ResponseEntity<DeviceTypeDTO> createDeviceType(@RequestBody DeviceTypeDTO deviceTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DeviceType : {}", deviceTypeDTO);
        if (deviceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceTypeDTO result = deviceTypeService.save(deviceTypeDTO);
        return ResponseEntity
            .created(new URI("/api/device-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /device-types/:id} : Updates an existing deviceType.
     *
     * @param id the id of the deviceTypeDTO to save.
     * @param deviceTypeDTO the deviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the deviceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-types/{id}")
    public ResponseEntity<DeviceTypeDTO> updateDeviceType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeviceTypeDTO deviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceType : {}, {}", id, deviceTypeDTO);
        if (deviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeviceTypeDTO result = deviceTypeService.save(deviceTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceTypeDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /device-types/:id} : Partial updates given fields of an existing deviceType, field will ignore if it is null
     *
     * @param id the id of the deviceTypeDTO to save.
     * @param deviceTypeDTO the deviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the deviceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/device-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceTypeDTO> partialUpdateDeviceType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeviceTypeDTO deviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceType partially : {}, {}", id, deviceTypeDTO);
        if (deviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceTypeDTO> result = deviceTypeService.partialUpdate(deviceTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceTypeDTO.getId())
        );
    }

    /**
     * {@code GET  /device-types} : get all the deviceTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceTypes in body.
     */
    @GetMapping("/device-types")
    public ResponseEntity<List<DeviceTypeDTO>> getAllDeviceTypes(Pageable pageable) {
        log.debug("REST request to get a page of DeviceTypes");
        Page<DeviceTypeDTO> page = deviceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-types/:id} : get the "id" deviceType.
     *
     * @param id the id of the deviceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-types/{id}")
    public ResponseEntity<DeviceTypeDTO> getDeviceType(@PathVariable String id) {
        log.debug("REST request to get DeviceType : {}", id);
        Optional<DeviceTypeDTO> deviceTypeDTO = deviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceTypeDTO);
    }

    /**
     * {@code DELETE  /device-types/:id} : delete the "id" deviceType.
     *
     * @param id the id of the deviceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-types/{id}")
    public ResponseEntity<Void> deleteDeviceType(@PathVariable String id) {
        log.debug("REST request to delete DeviceType : {}", id);
        deviceTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
