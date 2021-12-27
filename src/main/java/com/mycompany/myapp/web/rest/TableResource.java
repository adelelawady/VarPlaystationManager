package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.domain.Table;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.repository.TableRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.ReceiptPrint;
import com.mycompany.myapp.service.ReceiptTablePrint;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Table}.
 */
@RestController
@RequestMapping("/api")
public class TableResource {

    private final Logger log = LoggerFactory.getLogger(TableResource.class);

    private static final String ENTITY_NAME = "table";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableRepository tableRepository;

    private final ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    TableRecordRepository recordRepository;

    public TableResource(TableRepository tableRepository, ProductRepository productRepository) {
        this.tableRepository = tableRepository;
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /tables} : Create a new table.
     *
     * @param table the table to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new table, or with status {@code 400 (Bad Request)} if the
     *         table has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tables")
    public ResponseEntity<Table> createTable(@RequestBody Table table) throws URISyntaxException {
        log.debug("REST request to save Table : {}", table);
        if (table.getId() != null) {
            throw new BadRequestAlertException("A new table cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Table result = tableRepository.save(table);
        return ResponseEntity
            .created(new URI("/api/tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /tables/:id} : Updates an existing table.
     *
     * @param id    the id of the table to save.
     * @param table the table to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated table, or with status {@code 400 (Bad Request)} if the
     *         table is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the table couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tables/{id}")
    public ResponseEntity<Table> updateTable(@PathVariable(value = "id", required = false) final String id, @RequestBody Table table)
        throws URISyntaxException {
        log.debug("REST request to update Table : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Table result = tableRepository.save(table);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, table.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /tables/:id} : Partial updates given fields of an existing
     * table, field will ignore if it is null
     *
     * @param id    the id of the table to save.
     * @param table the table to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated table, or with status {@code 400 (Bad Request)} if the
     *         table is not valid, or with status {@code 404 (Not Found)} if the
     *         table is not found, or with status
     *         {@code 500 (Internal Server Error)} if the table couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Table> partialUpdateTable(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Table table
    ) throws URISyntaxException {
        log.debug("REST request to partial update Table partially : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Table> result = tableRepository
            .findById(table.getId())
            .map(existingTable -> {
                if (table.getName() != null) {
                    existingTable.setName(table.getName());
                }
                if (table.getDiscount() != null) {
                    existingTable.setDiscount(table.getDiscount());
                }
                if (table.getTotalPrice() != null) {
                    existingTable.setTotalPrice(table.getTotalPrice());
                }

                return existingTable;
            })
            .map(tableRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, table.getId()));
    }

    /**
     * {@code GET  /tables} : get all the tables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tables in body.
     */
    @GetMapping("/tables")
    public List<Table> getAllTables() {
        log.debug("REST request to get all Tables");
        return tableRepository.findAll();
    }

    /**
     * {@code GET  /tables/:id} : get the "id" table.
     *
     * @param id the id of the table to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the table, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tables/{id}")
    public ResponseEntity<Table> getTable(@PathVariable String id) {
        log.debug("REST request to get Table : {}", id);
        Optional<Table> table = tableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(table);
    }

    /**
     * {@code DELETE  /tables/:id} : delete the "id" table.
     *
     * @param id the id of the table to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tables/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable String id) {
        log.debug("REST request to delete Table : {}", id);
        tableRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/tables/start-session/{tableId}")
    public ResponseEntity<Table> startDeviceSession(@PathVariable String tableId) {
        Optional<Table> table = tableRepository.findById(tableId);

        if (table.isPresent()) {
            Table tableob = table.get();
            tableob.setActive(true);
            tableRepository.save(tableob);
            return ResponseEntity.ok(tableob);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tables/stop-session/{tableId}")
    public ResponseEntity<Table> stopDeviceSession(@PathVariable String tableId, @RequestBody SessionEndDTO dto) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isPresent()) {
            Table tableob = table.get();
            Double netTotalPrice = tableob.getTotalPrice();
            tableob.setDiscount(dto.getDiscount());
            tableob.setActive(false);

            Table savedTable = tableRepository.save(tableob);
            Table resTabel = calculateDeviceSessionOrderesPrice(savedTable);

            TableRecord tableRecord = new TableRecord();
            tableRecord.setDiscount(resTabel.getDiscount());

            tableRecord.setTotalPrice(resTabel.getTotalPrice());
            tableRecord.setOrdersData(resTabel.getOrdersData());
            tableRecord.setOrdersQuantity(resTabel.getOrdersQuantity());
            tableRecord.setTable(resTabel);
            tableRecord.setNetTotalPrice(netTotalPrice);

            TableRecord SavedTableRecord = recordRepository.save(tableRecord);

            resTabel.setActive(false);
            resTabel.setTotalPrice(0.0);
            resTabel.setDiscount(0.0);
            resTabel.setOrdersData(new HashSet<>());
            resTabel.setOrdersQuantity(new HashMap<>());
            Table savedTableX = tableRepository.save(resTabel);

            if (dto.isPrint()) {
                Printable printable = new ReceiptTablePrint(SavedTableRecord);

                PrinterSupport ps = new PrinterSupport();

                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.setPrintable(printable, ps.getPageFormat(pj));
                try {
                    pj.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }

            return ResponseEntity.ok(savedTableX);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/products/{productId}/add")
    public ResponseEntity<Table> addProductToDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isPresent()) {
            Table tableob = table.get();

            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                boolean sessionHasItem = tableob.getOrdersQuantity().containsKey(productId);
                if (sessionHasItem) {
                    int currentvalue = tableob.getOrdersQuantity().get(productId);
                    tableob.getOrdersQuantity().put(productId, currentvalue + 1);
                } else {
                    tableob.getOrdersQuantity().put(productId, 1);
                }
                tableob.getOrdersData().add(product.get());
                tableob.setActive(true);
                tableRepository.save(tableob);
                Table resTabel = calculateDeviceSessionOrderesPrice(tableob);
                return ResponseEntity.ok(resTabel);
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/products/{productId}/delete")
    public ResponseEntity<Table> deleteProductFromDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isPresent()) {
            Table tableob = table.get();

            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                boolean sessionHasItem = tableob.getOrdersQuantity().containsKey(productId);
                if (sessionHasItem) {
                    int currentvalue = tableob.getOrdersQuantity().get(productId);
                    if (currentvalue > 1) {
                        tableob.getOrdersQuantity().put(productId, currentvalue - 1);
                    } else {
                        tableob.getOrdersQuantity().remove(productId);
                        tableob.getOrdersData().remove(product.get());
                    }
                }

                if (tableob.getOrdersData().size() == 0) {
                    tableob.setActive(false);
                }
                tableRepository.save(tableob);
                Table resTabel = calculateDeviceSessionOrderesPrice(tableob);
                return ResponseEntity.ok(resTabel);
            }
        }
        return ResponseEntity.ok().build();
    }

    private Table calculateDeviceSessionOrderesPrice(Table table) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : table.getOrdersData()) {
            int prodValue;
            if (table.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = table.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 1;
            }

            Double prodPrice = order.getPrice();
            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }

        if (table.getDiscount() != null && table.getDiscount() > 0 && table.getDiscount() < 100) {
            totalCalculationsOfOrders = (double) Math.round((100 - table.getDiscount()) * totalCalculationsOfOrders / 100);
        }
        table.setTotalPrice(totalCalculationsOfOrders);
        return tableRepository.save(table);
    }
}
