package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SessionRepository;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.SessionDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Session}.
 */
@RestController
@RequestMapping("/api")
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);

    private static final String ENTITY_NAME = "session";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionService sessionService;

    private final SessionRepository sessionRepository;

    public SessionResource(SessionService sessionService, SessionRepository sessionRepository) {
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
    }

    /**
     * {@code POST  /sessions} : Create a new session.
     *
     * @param sessionDTO the sessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionDTO, or with status {@code 400 (Bad Request)} if the session has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sessions")
    public ResponseEntity<SessionDTO> createSession(@Valid @RequestBody SessionDTO sessionDTO) throws URISyntaxException {
        log.debug("REST request to save Session : {}", sessionDTO);
        if (sessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new session cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionDTO result = sessionService.save(sessionDTO);
        return ResponseEntity
            .created(new URI("/api/sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /sessions/:id} : Updates an existing session.
     *
     * @param id the id of the sessionDTO to save.
     * @param sessionDTO the sessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionDTO,
     * or with status {@code 400 (Bad Request)} if the sessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sessions/{id}")
    public ResponseEntity<SessionDTO> updateSession(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SessionDTO sessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Session : {}, {}", id, sessionDTO);
        if (sessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionDTO result = sessionService.save(sessionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /sessions/:id} : Partial updates given fields of an existing session, field will ignore if it is null
     *
     * @param id the id of the sessionDTO to save.
     * @param sessionDTO the sessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionDTO,
     * or with status {@code 400 (Bad Request)} if the sessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sessions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionDTO> partialUpdateSession(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SessionDTO sessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Session partially : {}, {}", id, sessionDTO);
        if (sessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionDTO> result = sessionService.partialUpdate(sessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionDTO.getId())
        );
    }

    /**
     * {@code GET  /sessions} : get all the sessions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessions in body.
     */
    @GetMapping("/sessions")
    public ResponseEntity<List<SessionDTO>> getAllSessions(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Sessions");
        Page<SessionDTO> page;
        if (eagerload) {
            page = sessionService.findAllWithEagerRelationships(pageable);
        } else {
            page = sessionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sessions/:id} : get the "id" session.
     *
     * @param id the id of the sessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sessions/{id}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable String id) {
        log.debug("REST request to get Session : {}", id);
        Optional<SessionDTO> sessionDTO = sessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionDTO);
    }

    /**
     * {@code DELETE  /sessions/:id} : delete the "id" session.
     *
     * @param id the id of the sessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        log.debug("REST request to delete Session : {}", id);
        sessionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
