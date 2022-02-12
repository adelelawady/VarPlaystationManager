package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Table.TABLE_TYPE;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ReceiptTablePrint;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TableRecord}.
 */
@RestController
@RequestMapping("/api")
public class TableRecordResource {

    private final Logger log = LoggerFactory.getLogger(TableRecordResource.class);

    private static final String ENTITY_NAME = "tableRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableRecordRepository tableRecordRepository;

    public TableRecordResource(TableRecordRepository tableRecordRepository) {
        this.tableRecordRepository = tableRecordRepository;
    }

    /**
     * {@code POST  /table-records} : Create a new tableRecord.
     *
     * @param tableRecord the tableRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tableRecord, or with status {@code 400 (Bad Request)} if the tableRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/table-records")
    public ResponseEntity<TableRecord> createTableRecord(@RequestBody TableRecord tableRecord) throws URISyntaxException {
        log.debug("REST request to save TableRecord : {}", tableRecord);
        if (tableRecord.getId() != null) {
            throw new BadRequestAlertException("A new tableRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TableRecord result = tableRecordRepository.save(tableRecord);
        return ResponseEntity
            .created(new URI("/api/table-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /table-records/:id} : Updates an existing tableRecord.
     *
     * @param id the id of the tableRecord to save.
     * @param tableRecord the tableRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tableRecord,
     * or with status {@code 400 (Bad Request)} if the tableRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tableRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/table-records/{id}")
    public ResponseEntity<TableRecord> updateTableRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TableRecord tableRecord
    ) throws URISyntaxException {
        log.debug("REST request to update TableRecord : {}, {}", id, tableRecord);
        if (tableRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TableRecord result = tableRecordRepository.save(tableRecord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tableRecord.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /table-records/:id} : Partial updates given fields of an existing tableRecord, field will ignore if it is null
     *
     * @param id the id of the tableRecord to save.
     * @param tableRecord the tableRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tableRecord,
     * or with status {@code 400 (Bad Request)} if the tableRecord is not valid,
     * or with status {@code 404 (Not Found)} if the tableRecord is not found,
     * or with status {@code 500 (Internal Server Error)} if the tableRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/table-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TableRecord> partialUpdateTableRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TableRecord tableRecord
    ) throws URISyntaxException {
        log.debug("REST request to partial update TableRecord partially : {}, {}", id, tableRecord);
        if (tableRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tableRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TableRecord> result = tableRecordRepository
            .findById(tableRecord.getId())
            .map(existingTableRecord -> {
                if (tableRecord.getTotalPrice() != null) {
                    existingTableRecord.setTotalPrice(tableRecord.getTotalPrice());
                }
                if (tableRecord.getTotalDiscountPrice() != null) {
                    existingTableRecord.setTotalDiscountPrice(tableRecord.getTotalDiscountPrice());
                }
                if (tableRecord.getNetTotalPrice() != null) {
                    existingTableRecord.setNetTotalPrice(tableRecord.getNetTotalPrice());
                }
                if (tableRecord.getDiscount() != null) {
                    existingTableRecord.setDiscount(tableRecord.getDiscount());
                }

                return existingTableRecord;
            })
            .map(tableRecordRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tableRecord.getId())
        );
    }

    /**
     * {@code GET  /table-records} : get all the tableRecords.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tableRecords in body.
     */
    @GetMapping("/table-records/type/{type}")
    public ResponseEntity<List<TableRecord>> getAllTableRecords(Pageable pageable, @PathVariable String type) {
        log.debug("REST request to get a page of TableRecords");
        Page<TableRecord> page = tableRecordRepository.findAllByTypeOrderByCreatedDateDesc(pageable, TABLE_TYPE.fromValue(type));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /table-records/:id} : get the "id" tableRecord.
     *
     * @param id the id of the tableRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tableRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/table-records/{id}")
    public ResponseEntity<TableRecord> getTableRecord(@PathVariable String id) {
        log.debug("REST request to get TableRecord : {}", id);
        Optional<TableRecord> tableRecord = tableRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tableRecord);
    }

    /**
     * {@code DELETE  /table-records/:id} : delete the "id" tableRecord.
     *
     * @param id the id of the tableRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/table-records/{id}")
    public ResponseEntity<Void> deleteTableRecord(@PathVariable String id) {
        log.debug("REST request to delete TableRecord : {}", id);
        tableRecordRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/table-records/print/{tebleId}")
    public ResponseEntity<Void> printTableRecord(@PathVariable String tebleId) {
        Optional<TableRecord> rec = tableRecordRepository.findById(tebleId);
        if (rec.isPresent()) {
            Printable printable = new ReceiptTablePrint(rec.get());

            PrinterSupport ps = new PrinterSupport();

            PrinterJob pj = PrinterJob.getPrinterJob();
            pj.setPrintable(printable, ps.getPageFormat(pj));
            try {
                pj.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }

        return ResponseEntity.ok().build();
    }
}
