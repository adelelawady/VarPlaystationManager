package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Takeaway;
import com.mycompany.myapp.repository.TakeawayRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Takeaway}.
 */
@RestController
@RequestMapping("/api")
public class TakeawayResource {

    private final Logger log = LoggerFactory.getLogger(TakeawayResource.class);

    private static final String ENTITY_NAME = "takeaway";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TakeawayRepository takeawayRepository;

    public TakeawayResource(TakeawayRepository takeawayRepository) {
        this.takeawayRepository = takeawayRepository;
    }

    /**
     * {@code POST  /takeaways} : Create a new takeaway.
     *
     * @param takeaway the takeaway to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new takeaway, or with status {@code 400 (Bad Request)} if the takeaway has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/takeaways")
    public ResponseEntity<Takeaway> createTakeaway(@RequestBody Takeaway takeaway) throws URISyntaxException {
        log.debug("REST request to save Takeaway : {}", takeaway);
        if (takeaway.getId() != null) {
            throw new BadRequestAlertException("A new takeaway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Takeaway result = takeawayRepository.save(takeaway);
        return ResponseEntity
            .created(new URI("/api/takeaways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /takeaways/:id} : Updates an existing takeaway.
     *
     * @param id the id of the takeaway to save.
     * @param takeaway the takeaway to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated takeaway,
     * or with status {@code 400 (Bad Request)} if the takeaway is not valid,
     * or with status {@code 500 (Internal Server Error)} if the takeaway couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/takeaways/{id}")
    public ResponseEntity<Takeaway> updateTakeaway(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Takeaway takeaway
    ) throws URISyntaxException {
        log.debug("REST request to update Takeaway : {}, {}", id, takeaway);
        if (takeaway.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, takeaway.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!takeawayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Takeaway result = takeawayRepository.save(takeaway);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, takeaway.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /takeaways/:id} : Partial updates given fields of an existing takeaway, field will ignore if it is null
     *
     * @param id the id of the takeaway to save.
     * @param takeaway the takeaway to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated takeaway,
     * or with status {@code 400 (Bad Request)} if the takeaway is not valid,
     * or with status {@code 404 (Not Found)} if the takeaway is not found,
     * or with status {@code 500 (Internal Server Error)} if the takeaway couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/takeaways/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Takeaway> partialUpdateTakeaway(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Takeaway takeaway
    ) throws URISyntaxException {
        log.debug("REST request to partial update Takeaway partially : {}, {}", id, takeaway);
        if (takeaway.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, takeaway.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!takeawayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Takeaway> result = takeawayRepository
            .findById(takeaway.getId())
            .map(existingTakeaway -> {
                if (takeaway.getTotalPrice() != null) {
                    existingTakeaway.setTotalPrice(takeaway.getTotalPrice());
                }

                return existingTakeaway;
            })
            .map(takeawayRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, takeaway.getId())
        );
    }

    /**
     * {@code GET  /takeaways} : get all the takeaways.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of takeaways in body.
     */
    @GetMapping("/takeaways")
    public List<Takeaway> getAllTakeaways() {
        log.debug("REST request to get all Takeaways");
        return takeawayRepository.findAll();
    }

    /**
     * {@code GET  /takeaways/:id} : get the "id" takeaway.
     *
     * @param id the id of the takeaway to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the takeaway, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/takeaways/{id}")
    public ResponseEntity<Takeaway> getTakeaway(@PathVariable String id) {
        log.debug("REST request to get Takeaway : {}", id);
        Optional<Takeaway> takeaway = takeawayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(takeaway);
    }

    /**
     * {@code DELETE  /takeaways/:id} : delete the "id" takeaway.
     *
     * @param id the id of the takeaway to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/takeaways/{id}")
    public ResponseEntity<Void> deleteTakeaway(@PathVariable String id) {
        log.debug("REST request to delete Takeaway : {}", id);
        takeawayRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
