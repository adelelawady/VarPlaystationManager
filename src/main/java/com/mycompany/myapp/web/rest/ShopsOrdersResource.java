package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ShopsOrders;
import com.mycompany.myapp.domain.Takeaway;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.ShopsOrdersRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ShopsOrders}.
 */
@RestController
@RequestMapping("/api")
public class ShopsOrdersResource {

    private final Logger log = LoggerFactory.getLogger(ShopsOrdersResource.class);

    private static final String ENTITY_NAME = "shopsOrders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopsOrdersRepository shopsOrdersRepository;

    @Autowired
    ProductRepository productRepository;

    public ShopsOrdersResource(ShopsOrdersRepository shopsOrdersRepository) {
        this.shopsOrdersRepository = shopsOrdersRepository;
    }

    /**
     * {@code POST  /shops-orders} : Create a new shopsOrders.
     *
     * @param shopsOrders the shopsOrders to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopsOrders, or with status {@code 400 (Bad Request)} if the shopsOrders has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shops-orders")
    public ResponseEntity<ShopsOrders> createShopsOrders(@RequestBody ShopsOrders shopsOrders) throws URISyntaxException {
        log.debug("REST request to save ShopsOrders : {}", shopsOrders);
        if (shopsOrders.getId() != null) {
            throw new BadRequestAlertException("A new shopsOrders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopsOrders result = shopsOrdersRepository.save(shopsOrders);
        return ResponseEntity
            .created(new URI("/api/shops-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /shops-orders/:id} : Updates an existing shopsOrders.
     *
     * @param id the id of the shopsOrders to save.
     * @param shopsOrders the shopsOrders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopsOrders,
     * or with status {@code 400 (Bad Request)} if the shopsOrders is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopsOrders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shops-orders/{id}")
    public ResponseEntity<ShopsOrders> updateShopsOrders(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ShopsOrders shopsOrders
    ) throws URISyntaxException {
        log.debug("REST request to update ShopsOrders : {}, {}", id, shopsOrders);
        if (shopsOrders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopsOrders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopsOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShopsOrders result = shopsOrdersRepository.save(shopsOrders);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopsOrders.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /shops-orders/:id} : Partial updates given fields of an existing shopsOrders, field will ignore if it is null
     *
     * @param id the id of the shopsOrders to save.
     * @param shopsOrders the shopsOrders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopsOrders,
     * or with status {@code 400 (Bad Request)} if the shopsOrders is not valid,
     * or with status {@code 404 (Not Found)} if the shopsOrders is not found,
     * or with status {@code 500 (Internal Server Error)} if the shopsOrders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shops-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShopsOrders> partialUpdateShopsOrders(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ShopsOrders shopsOrders
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShopsOrders partially : {}, {}", id, shopsOrders);
        if (shopsOrders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopsOrders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopsOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShopsOrders> result = shopsOrdersRepository
            .findById(shopsOrders.getId())
            .map(existingShopsOrders -> {
                if (shopsOrders.getName() != null) {
                    existingShopsOrders.setName(shopsOrders.getName());
                }
                if (shopsOrders.getTotalPrice() != null) {
                    existingShopsOrders.setTotalPrice(shopsOrders.getTotalPrice());
                }

                return existingShopsOrders;
            })
            .map(shopsOrdersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopsOrders.getId())
        );
    }

    /**
     * {@code GET  /shops-orders} : get all the shopsOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopsOrders in body.
     */
    @GetMapping("/shops-orders")
    public List<ShopsOrders> getAllShopsOrders() {
        log.debug("REST request to get all ShopsOrders");
        return shopsOrdersRepository.findTop20ByOrderByCreatedDateDesc();
    }

    /**
     * {@code GET  /shops-orders/:id} : get the "id" shopsOrders.
     *
     * @param id the id of the shopsOrders to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopsOrders, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shops-orders/{id}")
    public ResponseEntity<ShopsOrders> getShopsOrders(@PathVariable String id) {
        log.debug("REST request to get ShopsOrders : {}", id);
        Optional<ShopsOrders> shopsOrders = shopsOrdersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shopsOrders);
    }

    /**
     * {@code DELETE  /shops-orders/:id} : delete the "id" shopsOrders.
     *
     * @param id the id of the shopsOrders to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shops-orders/{id}")
    public ResponseEntity<Void> deleteShopsOrders(@PathVariable String id) {
        log.debug("REST request to delete ShopsOrders : {}", id);
        shopsOrdersRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/shops-orders/create/product/{productId}")
    public ResponseEntity<ShopsOrders> createNewTakeAwayFromProduct(@PathVariable String productId) {
        Optional<Product> prod = productRepository.findById(productId);

        if (prod.isPresent()) {
            ShopsOrders shopsOrders = new ShopsOrders();
            shopsOrders.setProduct(prod.get());
            shopsOrders.setTotalPrice(prod.get().getShopsPrice());
            ShopsOrders saved = shopsOrdersRepository.save(shopsOrders);
            return ResponseEntity.ok(saved);
        }

        return ResponseEntity.ok().build();
    }
}
