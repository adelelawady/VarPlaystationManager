package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Playground;
import com.mycompany.myapp.domain.PlaygroundTime;
import com.mycompany.myapp.repository.DeviceRepository;
import com.mycompany.myapp.repository.PlaygroundRepository;
import com.mycompany.myapp.service.DeviceService;
import com.mycompany.myapp.service.PlayGroundService;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Device}.
 */
@RestController
@RequestMapping("/api/playground")
public class PlayGroundResource {

    private final Logger log = LoggerFactory.getLogger(PlayGroundResource.class);

    private static final String ENTITY_NAME = "device";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayGroundService deviceService;

    private final PlaygroundRepository deviceRepository;

    public PlayGroundResource(PlayGroundService deviceService, PlaygroundRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param deviceDTO the deviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new deviceDTO, or with status {@code 400 (Bad Request)} if
     *         the device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public ResponseEntity<Playground> createDevice(@Valid @RequestBody Playground deviceDTO) throws URISyntaxException {
        log.debug("REST request to save Device : {}", deviceDTO);
        if (deviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Playground result = deviceService.save(deviceDTO);
        return ResponseEntity
            .created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing device.
     *
     * @param id        the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated deviceDTO, or with status {@code 400 (Bad Request)} if
     *         the deviceDTO is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the deviceDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices/{id}")
    public ResponseEntity<Playground> updateDevice(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Playground deviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Device : {}, {}", id, deviceDTO);
        if (deviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Playground> playground = deviceService.findOne(id);
        Playground result = deviceService.save(deviceDTO);
        result.setPlaygroundTimeList(playground.get().getPlaygroundTimeList());
        result = deviceService.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing
     * device, field will ignore if it is null
     *
     * @param id        the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated deviceDTO, or with status {@code 400 (Bad Request)} if
     *         the deviceDTO is not valid, or with status {@code 404 (Not Found)} if
     *         the deviceDTO is not found, or with status
     *         {@code 500 (Internal Server Error)} if the deviceDTO couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of devices in body.
     */
    @GetMapping("/devices")
    public ResponseEntity<List<Playground>> getAllDevices(Pageable pageable) {
        log.debug("REST request to get a page of Devices");
        Page<Playground> page = deviceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the deviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the deviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Playground> getDevice(@PathVariable String id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Playground> deviceDTO = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceDTO);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the deviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        log.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/devices-sessions")
    public ResponseEntity<List<Playground>> getAllDevicesWithSessions() {
        log.debug("REST request to get a page of Devices");
        List<Playground> listDevices = deviceService.devicesWithSessions();
        return ResponseEntity.ok(listDevices);
    }

    @GetMapping("/devices-sessions/{deviceId}")
    public ResponseEntity<Playground> getDeviceWithSession(@PathVariable String deviceId) {
        log.debug("REST request to get a page of Devices");
        Playground device = deviceService.deviceWithSession(deviceId);
        return ResponseEntity.ok(device);
    }

    @PostMapping("/start-session/{deviceId}")
    public ResponseEntity<Playground> startDeviceSession(@PathVariable String deviceId, @RequestBody SessionStartDTO sessionstart) {
        Playground listDevices = deviceService.startSession(deviceId, sessionstart);
        return ResponseEntity.ok(listDevices);
    }

    @PostMapping("/stop-session/{deviceId}")
    public ResponseEntity<Playground> stopDeviceSession(@PathVariable String deviceId, @RequestBody SessionEndDTO sessionend) {
        Playground listDevices = deviceService.stopSession(deviceId, sessionend);
        return ResponseEntity.ok(listDevices);
    }

    @GetMapping("/device/{deviceId}/session/product/{productId}/add")
    public ResponseEntity<Playground> addProductToDeviceSession(@PathVariable String deviceId, @PathVariable String productId) {
        Playground listDevices = deviceService.addProductToDeviceSession(deviceId, productId);
        return ResponseEntity.ok(listDevices);
    }

    @GetMapping("/device/{deviceId}/session/product/{productId}/delete")
    public ResponseEntity<Playground> deleteProductFromDeviceSession(@PathVariable String deviceId, @PathVariable String productId) {
        Playground listDevices = deviceService.deleteProductFromDeviceSession(deviceId, productId);
        return ResponseEntity.ok(listDevices);
    }

    @PostMapping("/devices/{plId}/add-time")
    public ResponseEntity<Playground> addProductFromDeviceSession(@PathVariable String plId, @RequestBody PlaygroundTime playgroundTime) {
        Optional<Playground> playground = deviceService.findOne(plId);
        if (playground.isPresent()) {
            Playground pl = playground.get();
            pl.getPlaygroundTimeList().add(playgroundTime);
            return ResponseEntity.ok(deviceService.save(pl));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/devices/{plId}/remove-time")
    public ResponseEntity<Playground> deleteProductFromDeviceSession(
        @PathVariable String plId,
        @RequestBody PlaygroundTime playgroundTime
    ) {
        Optional<Playground> playground = deviceService.findOne(plId);
        if (playground.isPresent()) {
            Playground pl = playground.get();
            pl
                .getPlaygroundTimeList()
                .removeIf(playgroundTime1 ->
                    playgroundTime1.getPrice().equals(playgroundTime.getPrice()) &&
                    playgroundTime1.getFrom().equals(playgroundTime.getFrom()) &&
                    playgroundTime1.getTo().equals(playgroundTime.getTo())
                );
            return ResponseEntity.ok(deviceService.save(pl));
        }
        return ResponseEntity.ok().build();
    }
}
