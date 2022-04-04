package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.Table.TABLE_TYPE;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Table}.
 */
@RestController
@RequestMapping("/api/pos")
public class OrderHandlerResource {

    private final Logger log = LoggerFactory.getLogger(OrderHandlerResource.class);

    private static final String ENTITY_NAME = "table";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderHandlerRepository orderHandlerRepository;

    private final ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PosOrderRecoredRepository recordRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    TableRecordRepository tableRecordRepository;

    public OrderHandlerResource(OrderHandlerRepository orderHandlerRepository, ProductRepository productRepository) {
        this.orderHandlerRepository = orderHandlerRepository;
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
    public ResponseEntity<OrderHandlerDomain> createTable(@RequestBody OrderHandlerDomain table) throws URISyntaxException {
        log.debug("REST request to save Table : {}", table);
        if (table.getId() != null) {
            throw new BadRequestAlertException("A new table cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderHandlerDomain result = orderHandlerRepository.save(table);
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
    public ResponseEntity<OrderHandlerDomain> updateTable(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody OrderHandlerDomain table
    ) throws URISyntaxException {
        log.debug("REST request to update Table : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderHandlerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderHandlerDomain result = orderHandlerRepository.save(table);
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
    public ResponseEntity<OrderHandlerDomain> partialUpdateTable(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody OrderHandlerDomain table
    ) throws URISyntaxException {
        log.debug("REST request to partial update Table partially : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderHandlerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderHandlerDomain> result = orderHandlerRepository
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
            .map(orderHandlerRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, table.getId()));
    }

    /**
     * {@code GET  /tables} : get all the tables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tables in body.
     */
    @GetMapping("/tables")
    public List<OrderHandlerDomain> getAllTables() {
        log.debug("REST request to get all Tables");
        return orderHandlerRepository.findAll();
    }

    @GetMapping("/tables/{type}/getall")
    public List<OrderHandlerDomain> getAllTablesByType(@PathVariable String type) {
        log.debug("REST request to get all Tables");
        return orderHandlerRepository.findAllByTypeOrderByIndex(type.toUpperCase());
    }

    @GetMapping("/tables/fixTablesRecords")
    public ResponseEntity<String> fixTablesRecords() {
        tableRecordRepository
            .findAll()
            .stream()
            .forEach(tb -> {
                if (tb.getType() == null) {
                    tb.setType(TABLE_TYPE.TABLE);
                    tableRecordRepository.save(tb);
                }
            });

        return ResponseEntity.ok("done");
    }

    /**
     * {@code GET  /tables/:id} : get the "id" table.
     *
     * @param id the id of the table to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the table, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tables/{id}")
    public ResponseEntity<OrderHandlerDomain> getTable(@PathVariable String id) {
        log.debug("REST request to get Table : {}", id);
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(id);
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
        orderHandlerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/tables/start-session/{tableId}")
    public ResponseEntity<OrderHandlerDomain> startTableSession(@PathVariable String tableId) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);

        if (table.isPresent()) {
            OrderHandlerDomain tableob = table.get();
            tableob.setActive(true);
            orderHandlerRepository.save(tableob);
            return ResponseEntity.ok(tableob);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tables/stop-session/{tableId}")
    public ResponseEntity<OrderHandlerDomain> stopTableSession(@PathVariable String tableId, @RequestBody SessionEndDTO dto) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);
        if (table.isPresent()) {
            OrderHandlerDomain tableob = table.get();
            Double netTotalPrice = tableob.getTotalPrice();
            tableob.setDiscount(dto.getOrdersDiscount());
            tableob.setActive(false);

            OrderHandlerDomain savedTable = orderHandlerRepository.save(tableob);
            OrderHandlerDomain resTabel = calculateDeviceSessionOrderesPrice(savedTable);

            PosOrderRecord tableRecord = new PosOrderRecord();
            tableRecord.setDiscount(resTabel.getDiscount());

            tableRecord.setTotalPrice(dto.getTotalPrice());
            tableRecord.setOrdersData(resTabel.getOrdersData());
            tableRecord.setOrdersQuantity(resTabel.getOrdersQuantity());
            tableRecord.setTable(resTabel);
            tableRecord.setNetTotalPrice(calculateNetDeviceSessionOrderesPrice(resTabel));
            tableRecord.setType(resTabel.getType());
            if (resTabel.getDiscount() != null && resTabel.getDiscount() > 0) {
                tableRecord.setTotalDiscountPrice((tableRecord.getNetTotalPrice() * resTabel.getDiscount()) / 100);
            } else {
                tableRecord.setTotalDiscountPrice(0.0);
            }

            PosOrderRecord SavedTableRecord = recordRepository.save(tableRecord);

            OrderHandlerDomain savedTableX = resetTable(resTabel);

            orderHandlerRepository.delete(savedTableX);

            if (dto.isPrint()) {
                Printable printable = new ReceiptPosPrint(SavedTableRecord);

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
    public ResponseEntity<OrderHandlerDomain> addProductToDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);
        if (table.isPresent()) {
            OrderHandlerDomain tableob = table.get();

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
                orderHandlerRepository.save(tableob);
                OrderHandlerDomain resTabel = calculateDeviceSessionOrderesPrice(tableob);
                return ResponseEntity.ok(resTabel);
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/products/{productId}/delete")
    public ResponseEntity<OrderHandlerDomain> deleteProductFromDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);
        if (table.isPresent()) {
            OrderHandlerDomain tableob = table.get();

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

                if (tableob.getOrdersData().isEmpty()) {
                    // tableob.setActive(false);
                    orderHandlerRepository.delete(tableob);
                    return null;
                } else {
                    orderHandlerRepository.save(tableob);
                }

                OrderHandlerDomain resTabel = calculateDeviceSessionOrderesPrice(tableob);
                return ResponseEntity.ok(resTabel);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/devices/{deviceId}/move")
    public ResponseEntity<OrderHandlerDomain> modeTableToDevice(@PathVariable String tableId, @PathVariable String deviceId) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);
        if (table.isPresent()) {
            OrderHandlerDomain tableObject = table.get();

            Optional<Device> deviceFound = deviceRepository.findById(deviceId);

            Session sess = deviceService.getDeviceActiveSession(deviceId);
            if (sess != null) {
                // device active

                // add products to orders data

                addProductsToSession(sess, tableObject);

                // empty table
                resetTable(tableObject);
            } else {
                // device not active
                SessionStartDTO sessionStartDTO = new SessionStartDTO();
                sessionStartDTO.setMulti(false);
                deviceService.startSession(deviceId, sessionStartDTO);
                // add products to orders data
                Session activeSession = deviceService.getDeviceActiveSession(deviceId);
                addProductsToSession(activeSession, tableObject);

                resetTable(tableObject);
            }
        }
        Optional<OrderHandlerDomain> tableRes = orderHandlerRepository.findById(tableId);
        if (tableRes.isPresent()) {
            return ResponseEntity.ok(tableRes.get());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/tables/{tableIdToMoveTo}/move")
    public ResponseEntity<OrderHandlerDomain> modeTableToTable(@PathVariable String tableId, @PathVariable String tableIdToMoveTo) {
        Optional<OrderHandlerDomain> table = orderHandlerRepository.findById(tableId);
        if (table.isPresent()) {
            OrderHandlerDomain tableObject = table.get();

            Optional<OrderHandlerDomain> tableToMoveTo = orderHandlerRepository.findById(tableIdToMoveTo);

            if (tableToMoveTo.isPresent()) {
                moveOrdersTableToTable(table.get(), tableToMoveTo.get());
            }

            // empty table One
            resetTable(tableObject);
        }
        Optional<OrderHandlerDomain> tableRes = orderHandlerRepository.findById(tableId);
        if (tableRes.isPresent()) {
            return ResponseEntity.ok(tableRes.get());
        }
        return ResponseEntity.ok().build();
    }

    OrderHandlerDomain resetTable(OrderHandlerDomain table) {
        table.setActive(false);
        table.setTotalPrice(0.0);
        table.setDiscount(0.0);
        table.setOrdersData(new HashSet<>());
        table.setOrdersQuantity(new HashMap<>());
        return orderHandlerRepository.save(table);
    }

    Session addProductsToSession(Session sess, OrderHandlerDomain table) {
        // add all missing products
        sess.getOrders().addAll(table.getOrdersData());
        for (Product prodToAdd : table.getOrdersData()) {
            boolean sessionHasItem = sess.getOrdersQuantity().containsKey(prodToAdd.getId());
            int currentvalueTable = table.getOrdersQuantity().get(prodToAdd.getId());
            if (sessionHasItem) {
                int currentvalueDevice = sess.getOrdersQuantity().get(prodToAdd.getId());
                sess.getOrdersQuantity().put(prodToAdd.getId(), currentvalueDevice + currentvalueTable);
            } else {
                sess.getOrdersQuantity().put(prodToAdd.getId(), currentvalueTable);
            }
        }
        sessionService.calculateDeviceSessionOrderesPrice(sess);

        Optional<Device> deviceOp = deviceRepository.findById(sess.getDeviceId());

        if (!deviceOp.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }

        Device device = deviceOp.get();

        return device.getSession();
    }

    OrderHandlerDomain moveOrdersTableToTable(OrderHandlerDomain tableFrom, OrderHandlerDomain tableTo) {
        // add all missing products
        tableTo.getOrdersData().addAll(tableFrom.getOrdersData());

        for (Product prodToAdd : tableFrom.getOrdersData()) {
            boolean sessionHasItem = tableTo.getOrdersQuantity().containsKey(prodToAdd.getId());
            int currentvalueFromTable = tableFrom.getOrdersQuantity().get(prodToAdd.getId());
            if (sessionHasItem) {
                int currentvalueToTable = tableTo.getOrdersQuantity().get(prodToAdd.getId());
                tableTo.getOrdersQuantity().put(prodToAdd.getId(), currentvalueToTable + currentvalueFromTable);
            } else {
                tableTo.getOrdersQuantity().put(prodToAdd.getId(), currentvalueFromTable);
            }
        }
        tableTo.setActive(true);
        OrderHandlerDomain savedTable = orderHandlerRepository.save(tableTo);
        return calculateDeviceSessionOrderesPrice(savedTable);
    }

    private OrderHandlerDomain calculateDeviceSessionOrderesPrice(OrderHandlerDomain table) {
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
        return orderHandlerRepository.save(table);
    }

    private Double calculateNetDeviceSessionOrderesPrice(OrderHandlerDomain table) {
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
        return totalCalculationsOfOrders;
    }
}
