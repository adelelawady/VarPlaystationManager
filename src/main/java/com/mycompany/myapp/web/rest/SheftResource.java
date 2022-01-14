package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Sheft;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.repository.SheftRepository;
import com.mycompany.myapp.repository.ShopsOrdersRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.repository.TakeawayRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ReceiptPrint;
import com.mycompany.myapp.service.ReceiptSheftPrint;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class SheftResource {

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    TableRecordRepository tableRecordRepository;

    @Autowired
    ShopsOrdersRepository shopsOrdersRepository;

    @Autowired
    TakeawayRepository takeawayRepository;

    @Autowired
    SheftRepository sheftRepository;

    @Autowired
    UserService userService;

    private final Logger log = LoggerFactory.getLogger(SheftResource.class);

    private static final String ENTITY_NAME = "sheft";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new productDTO, or with status {@code 400 (Bad Request)} if
     *         the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping("/sheft/start")
    public ResponseEntity<Sheft> startSheft() {
        if (sheftRepository.findAllByEnd(null).isEmpty()) {
            Sheft sheft = new Sheft();

            final Optional<User> isUser = userService.getUserWithAuthorities();
            if (!isUser.isPresent()) {
                return null;
            }

            final User user = isUser.get();
            sheft.setUser(user);

            Sheft sheftSaved = sheftRepository.save(sheft);

            ResponseEntity.ok(sheftRepository.findById(sheftSaved.getId()).get());
        } else {
            return ResponseEntity.ok(sheftRepository.findAllByEnd(null).get(0));
        }
        return null;
    }

    @GetMapping("/sheft/current")
    public ResponseEntity<Sheft> current() {
        if (sheftRepository.findAllByEnd(null).isEmpty()) {
            return null;
        } else {
            //stopSheft(sheftRepository.findAllByEnd(null).get(0).getId());
            //Optional<Sheft> res = sheftRepository.findById(sheftRepository.findAllByEnd(null).get(0).getId());
            return ResponseEntity.ok(sheftRepository.findAllByEnd(null).get(0));
        }
    }

    @GetMapping("/sheft/current/updated")
    public ResponseEntity<Sheft> currentUpdated() {
        if (sheftRepository.findAllByEnd(null).isEmpty()) {
            return null;
        } else {
            stopSheft(sheftRepository.findAllByEnd(null).get(0).getId());
            Optional<Sheft> res = sheftRepository.findById(sheftRepository.findAllByEnd(null).get(0).getId());
            return ResponseEntity.ok(res.get());
        }
    }

    @GetMapping("/sheft/last")
    public ResponseEntity<List<Sheft>> last() {
        return ResponseEntity.ok(sheftRepository.findTop20ByOrderByCreatedDateDesc().stream().collect(Collectors.toList()));
    }

    @GetMapping("/sheft/stop")
    public ResponseEntity<Sheft> stop() {
        Sheft activeSheft = null;
        String sheftId = "";
        if (sheftRepository.findAllByEnd(null).isEmpty()) {
            return null;
        } else {
            activeSheft = sheftRepository.findAllByEnd(null).get(0);
            activeSheft.setEnd(Instant.now());
            activeSheft = sheftRepository.save(activeSheft);
            sheftId = activeSheft.getId();
            stopSheft(activeSheft.getId());
        }

        sheftRepository
            .findAllByEnd(null)
            .stream()
            .forEach(sheft -> {
                sheft.setEnd(Instant.now());
                sheftRepository.save(sheft);
            });
        printSheft(sheftId);
        return ResponseEntity.ok(activeSheft);
    }

    private void stopSheft(String sheftId) {
        Optional<Sheft> sheftFound = sheftRepository.findById(sheftId);
        if (sheftFound.isPresent()) {
            Sheft sheftObj = sheftFound.get();

            List<Record> records = recordRepository.findAllByEndBetween(
                sheftFound.get().getStart(),
                sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
            );
            sheftObj.setRecords(records);

            List<TableRecord> tableRecords = tableRecordRepository.findAllByTypeAndCreatedDateBetween(
                "TABLE",
                sheftFound.get().getStart(),
                sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
            );

            List<TableRecord> takeawayRecords = tableRecordRepository.findAllByTypeAndCreatedDateBetween(
                "TAKEAWAY",
                sheftFound.get().getStart(),
                sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
            );

            List<TableRecord> shopsRecords = tableRecordRepository.findAllByTypeAndCreatedDateBetween(
                "SHOPS",
                sheftFound.get().getStart(),
                sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
            );

            sheftObj.setTableRecords(tableRecords);
            sheftObj.setTableTakeAwayRecords(takeawayRecords);
            sheftObj.setTableShopsRecords(shopsRecords);

            // DEVICES

            Double total_net_price_devices = records
                .stream()
                .map(Record::getTotalNetPriceCalculated)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_net_user_price_devices = records.stream().map(Record::getTotalPriceUser).mapToDouble(Double::doubleValue).sum();

            Double total_discount_price_devices = records
                .stream()
                .map(Record::getTotalDiscountPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_price_time_devices = records.stream().map(Record::getTotalPriceTime).mapToDouble(Double::doubleValue).sum();

            Double total_price_orders_devices = records.stream().map(Record::getTotalPriceOrders).mapToDouble(Double::doubleValue).sum();

            sheftObj.setTotal_net_price_devices(total_net_price_devices);

            sheftObj.setTotal_net_user_price_devices(total_net_user_price_devices);

            sheftObj.setTotal_discount_price_devices(total_discount_price_devices);

            sheftObj.setTotal_price_time_devices(total_price_time_devices);

            sheftObj.setTotal_price_orders_devices(total_price_orders_devices);

            // TABLES

            //totalPrice == user input
            // net price all actualprice

            Double total_net_price_Tables = tableRecords.stream().map(TableRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();

            Double total_discount_price_Tables = tableRecords
                .stream()
                .map(TableRecord::getTotalDiscountPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_net_price_after_discount_Tables = tableRecords
                .stream()
                .map(TableRecord::getTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_net_price_after_discount_Tables_System = total_net_price_Tables - total_discount_price_Tables;

            sheftObj.setTotal_net_price_Tables(total_net_price_Tables);
            sheftObj.setTotal_discount_price_Tables(total_discount_price_Tables);
            sheftObj.setTotal_net_price_after_discount_Tables(total_net_price_after_discount_Tables);
            // TakeAway
            Double total_net_price_takeaway = takeawayRecords
                .stream()
                .map(TableRecord::getNetTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
            Double total_discount_price_takeaway = takeawayRecords
                .stream()
                .map(TableRecord::getTotalDiscountPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
            Double total_net_price_after_discount_takeaway = takeawayRecords
                .stream()
                .map(TableRecord::getTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_net_price_after_discount_Tables_TakeAway_System = total_net_price_takeaway - total_discount_price_takeaway;

            sheftObj.setTotal_net_price_takeaway(total_net_price_takeaway);
            sheftObj.setTotal_discount_price_takeaway(total_discount_price_takeaway);
            sheftObj.setTotal_net_price_after_discount_takeaway(total_net_price_after_discount_takeaway);
            // shops
            Double total_net_price_shops = shopsRecords.stream().map(TableRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();
            Double total_discount_price_shops = shopsRecords
                .stream()
                .map(TableRecord::getTotalDiscountPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
            Double total_net_price_after_discount_shops = shopsRecords
                .stream()
                .map(TableRecord::getTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_net_price_after_discount_Tables_Shops_System = total_net_price_shops - total_discount_price_shops;

            sheftObj.setTotal_net_price_shops(total_net_price_shops);
            sheftObj.setTotal_discount_price_shops(total_discount_price_shops);
            sheftObj.setTotal_net_price_after_discount_shops(total_net_price_after_discount_shops);

            Double total_net_price = total_net_price_shops + total_net_price_takeaway + total_net_price_Tables + total_net_price_devices;

            Double total_discount =
                total_discount_price_shops + total_discount_price_takeaway + total_discount_price_Tables + total_discount_price_devices;

            Double total_net_price_after_discount =
                total_net_price_after_discount_shops +
                total_net_price_after_discount_takeaway +
                total_net_price_after_discount_Tables +
                total_net_user_price_devices;

            Double total_net_price_after_discountSystem =
                (total_net_price_devices - total_discount_price_devices) +
                total_net_price_after_discount_Tables_System +
                total_net_price_after_discount_Tables_TakeAway_System +
                total_net_price_after_discount_Tables_Shops_System;

            sheftObj.setTotal_net_price(total_net_price);

            sheftObj.setTotal_discount(total_discount);

            sheftObj.setTotal_net_price_after_discount(total_net_price_after_discount);

            sheftObj.setTotal_net_price_after_discount_system(total_net_price_after_discountSystem);

            sheftRepository.save(sheftObj);
        }
    }

    /**
     * {@code POST  /shefts} : Create a new sheft.
     *
     * @param sheft the sheft to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sheft, or with status {@code 400 (Bad Request)} if the sheft has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shefts")
    public ResponseEntity<Sheft> createSheft(@RequestBody Sheft sheft) throws URISyntaxException {
        log.debug("REST request to save Sheft : {}", sheft);
        if (sheft.getId() != null) {
            throw new BadRequestAlertException("A new sheft cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sheft result = sheftRepository.save(sheft);
        return ResponseEntity
            .created(new URI("/api/shefts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /shefts/:id} : Updates an existing sheft.
     *
     * @param id the id of the sheft to save.
     * @param sheft the sheft to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sheft,
     * or with status {@code 400 (Bad Request)} if the sheft is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sheft couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shefts/{id}")
    public ResponseEntity<Sheft> updateSheft(@PathVariable(value = "id", required = false) final String id, @RequestBody Sheft sheft)
        throws URISyntaxException {
        log.debug("REST request to update Sheft : {}, {}", id, sheft);
        if (sheft.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sheft.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sheftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sheft result = sheftRepository.save(sheft);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sheft.getId()))
            .body(result);
    }

    /**
     * {@code GET  /shefts} : get all the shefts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shefts in body.
     */
    @GetMapping("/shefts")
    public ResponseEntity<List<Sheft>> getAllShefts(Pageable pageable) {
        log.debug("REST request to get a page of Shefts");
        Page<Sheft> page = sheftRepository.findAllByOrderByCreatedDateDesc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shefts/:id} : get the "id" sheft.
     *
     * @param id the id of the sheft to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sheft, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shefts/{id}")
    public ResponseEntity<Sheft> getSheft(@PathVariable String id) {
        log.debug("REST request to get Sheft : {}", id);
        Optional<Sheft> sheft = sheftRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sheft);
    }

    /**
     * {@code DELETE  /shefts/:id} : delete the "id" sheft.
     *
     * @param id the id of the sheft to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shefts/{id}")
    public ResponseEntity<Void> deleteSheft(@PathVariable String id) {
        log.debug("REST request to delete Sheft : {}", id);
        sheftRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    public void printSheft(String recId) {
        Optional<com.mycompany.myapp.domain.Sheft> record = sheftRepository.findById(recId);

        if (!record.isPresent()) {
            return;
        }
        Printable printable = new ReceiptSheftPrint(record.get());

        PrinterSupport ps = new PrinterSupport();

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(printable, ps.getPageFormat(pj));
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }
}
