package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.*;
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
import java.time.ZoneId;
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

    @Autowired
    PosOrderRecoredRepository posOrderRecoredRepository;

    @Autowired
    PlaygroundRecoredRepository playgroundRepository;

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

            List<PosOrderRecord> allPosOrderRecCafe =
                this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                        "CAFE",
                        sheftFound.get().getStart(),
                        sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
                    );

            List<PosOrderRecord> allPosOrderRecMarket =
                this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                        "MARKET",
                        sheftFound.get().getStart(),
                        sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
                    );

            List<PosOrderRecord> allPosOrderRecRes =
                this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                        "RES",
                        sheftFound.get().getStart(),
                        sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
                    );

            List<PlaygroundRecord> allPlayGroundsrecords =
                this.playgroundRepository.findAllByStartBetween(
                        sheftFound.get().getStart(),
                        sheftFound.get().getEnd() == null ? Instant.now() : sheftFound.get().getEnd()
                    );

            Double TotalPricePlayGroundTime = allPlayGroundsrecords
                .stream()
                .map(PlaygroundRecord::getTotalPriceTime)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double TotalPriceCafe = allPosOrderRecCafe
                .stream()
                .map(PosOrderRecord::getNetTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
            Double TotalPriceMarket = allPosOrderRecMarket
                .stream()
                .map(PosOrderRecord::getNetTotalPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
            Double TotalPriceRes = allPosOrderRecRes.stream().map(PosOrderRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();

            sheftObj.setTableRecords(tableRecords);
            sheftObj.setMarketRecords(allPosOrderRecMarket);
            sheftObj.setResRecords(allPosOrderRecRes);
            sheftObj.setCafeRecords(allPosOrderRecCafe);
            sheftObj.setPlaygroundRecords(allPlayGroundsrecords);
            // DEVICES
            Double total_net_price_Tables = tableRecords.stream().map(TableRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();
            Double total_price_time_devices = records.stream().map(Record::getTotalPriceTime).mapToDouble(Double::doubleValue).sum();

            sheftObj.setTotal_net_price_Tables(total_net_price_Tables);
            sheftObj.setTotal_net_price_cafe(TotalPriceCafe);
            sheftObj.setTotal_net_price_market(TotalPriceMarket);
            sheftObj.setTotal_net_price_res(TotalPriceRes);
            sheftObj.setTotal_net_price_playground(TotalPricePlayGroundTime);

            // TABLES

            //totalPrice == user input
            // net price all actualprice

            sheftObj.setTotal_net_price(
                TotalPricePlayGroundTime +
                TotalPriceCafe +
                TotalPriceMarket +
                TotalPriceRes +
                //  total_net_price_Tables+
                total_price_time_devices
            );

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
