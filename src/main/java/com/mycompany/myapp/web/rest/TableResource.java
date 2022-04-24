package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.domain.Table;
import com.mycompany.myapp.domain.Table.TABLE_TYPE;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.repository.DeviceRepository;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.repository.TableRepository;
import com.mycompany.myapp.service.DeviceService;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.ReceiptTablePrint;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
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

    @Autowired
    SessionService sessionService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    TableRecordRepository tableRecordRepository;

    public TableResource(TableRepository tableRepository, ProductRepository productRepository) {
        this.tableRepository = tableRepository;
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /tables} : Create a new table.
     *
     * @param table the table to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new table, or with status {@code 400 (Bad Request)} if the
     * table has already an ID.
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
     * the updated table, or with status {@code 400 (Bad Request)} if the
     * table is not valid, or with status
     * {@code 500 (Internal Server Error)} if the table couldn't be updated.
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
     * the updated table, or with status {@code 400 (Bad Request)} if the
     * table is not valid, or with status {@code 404 (Not Found)} if the
     * table is not found, or with status
     * {@code 500 (Internal Server Error)} if the table couldn't be updated.
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
     * of tables in body.
     */
    @GetMapping("/tables")
    public List<Table> getAllTables() {
        log.debug("REST request to get all Tables");
        return tableRepository.findAll();
    }

    @GetMapping("/tables/{type}/getall")
    public List<Table> getAllTablesByType(@PathVariable String type) {
        log.debug("REST request to get all Tables");
        return tableRepository.findAllByTypeOrderByIndex(type.toUpperCase());
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
     * the table, or with status {@code 404 (Not Found)}.
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
    public ResponseEntity<Table> startTableSession(@PathVariable String tableId) {
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
    public ResponseEntity<Table> stopTableSession(@PathVariable String tableId, @RequestBody SessionEndDTO dto) {
        Optional<Table> table = tableRepository.findById(tableId);

        if (table.isPresent()) {
            if (table.get().getPaidOrdersPrice() > 0) {
                return ResponseEntity.ok(table.get());
            }

            Table tableob = table.get();
            Double netTotalPrice = tableob.getTotalPrice();
            tableob.setDiscount(dto.getOrdersDiscount());
            tableob.setActive(false);

            Table savedTable = tableRepository.save(tableob);
            Table resTabel = calculateDeviceSessionOrderesPrice(savedTable);

            TableRecord tableRecord = new TableRecord();
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

            TableRecord SavedTableRecord = recordRepository.save(tableRecord);

            Table savedTableX = resetTable(resTabel);

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

        if (table.get().getPaidOrdersPrice() > 0) {
            return ResponseEntity.ok(table.get());
        }

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

                if (tableob.getOrdersData().isEmpty()) {
                    tableob.setActive(false);
                }
                tableRepository.save(tableob);
                Table resTabel = calculateDeviceSessionOrderesPrice(tableob);
                return ResponseEntity.ok(resTabel);
            }
        }
        return ResponseEntity.ok().build();
    }

    public void deleteProductFromDeviceSessionInAction(@PathVariable String tableId, @PathVariable String productId) {
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

                if (tableob.getOrdersData().isEmpty()) {
                    tableob.setActive(false);
                }
                tableRepository.save(tableob);
                Table resTabel = calculateDeviceSessionOrderesPrice(tableob);
            }
        }
    }

    @GetMapping("/tables/{tableId}/devices/{deviceId}/move")
    public ResponseEntity<Table> modeTableToDevice(@PathVariable String tableId, @PathVariable String deviceId) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isPresent()) {
            Table tableObject = table.get();

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
        Optional<Table> tableRes = tableRepository.findById(tableId);
        if (tableRes.isPresent()) {
            return ResponseEntity.ok(tableRes.get());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/tables/{tableIdToMoveTo}/move")
    public ResponseEntity<Table> modeTableToTable(@PathVariable String tableId, @PathVariable String tableIdToMoveTo) {
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isPresent()) {
            Table tableObject = table.get();

            Optional<Table> tableToMoveTo = tableRepository.findById(tableIdToMoveTo);

            if (tableToMoveTo.isPresent()) {
                moveOrdersTableToTable(table.get(), tableToMoveTo.get());
            }

            // empty table One
            resetTable(tableObject);
        }
        Optional<Table> tableRes = tableRepository.findById(tableId);
        if (tableRes.isPresent()) {
            return ResponseEntity.ok(tableRes.get());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tables/{tableId}/session/product/{productId}/pay")
    public ResponseEntity<Table> payProductToDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Table listDevices = payProductfromTableSession(tableId, productId);
        return ResponseEntity.ok(listDevices);
    }

    @GetMapping("/tables/{tableId}/session/product/{productId}/unpay")
    public ResponseEntity<Table> unPayProductToDeviceSession(@PathVariable String tableId, @PathVariable String productId) {
        Table listDevices = unPayProductFromTableSession(tableId, productId);
        return ResponseEntity.ok(listDevices);
    }

    @GetMapping("/tables/{tableId}/session/product/pay/{print}/complete")
    public ResponseEntity<Table> completePayDevicePaidOrdersRest(@PathVariable String tableId, @PathVariable boolean print) {
        Table listDevices = completePayDevicePaidOrders(tableId, print);
        return ResponseEntity.ok(listDevices);
    }

    Table resetTable(Table table) {
        table.setActive(false);
        table.setTotalPrice(0.0);
        table.setDiscount(0.0);
        table.setOrdersData(new HashSet<>());
        table.setOrdersQuantity(new HashMap<>());
        table.setPaidOrdersPrice(0.0);
        table.setPaidOrdersQuantity(new HashMap<>());
        return tableRepository.save(table);
    }

    Session addProductsToSession(Session sess, Table table) {
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

    Table moveOrdersTableToTable(Table tableFrom, Table tableTo) {
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
        Table savedTable = tableRepository.save(tableTo);
        return calculateDeviceSessionOrderesPrice(savedTable);
    }

    private Table calculateDeviceSessionOrderesPrice(Table table) {
        Double totalCalculationsOfOrders = 0.0;
        Double totalPaidCalculationsOfOrders = 0.0;
        for (Product order : table.getOrdersData()) {
            int prodValue, prodPaiedValue;
            prodValue = table.getOrdersQuantity().getOrDefault(order.getId(), 1);
            prodPaiedValue = table.getPaidOrdersQuantity().getOrDefault(order.getId(), 0);

            Double prodPrice = order.getPrice();
            switch (table.getType()) {
                case TABLE:
                    prodPrice = order.getPrice();
                    break;
                case SHOPS:
                    prodPrice = order.getShopsPrice();
                    break;
                case TAKEAWAY:
                    prodPrice = order.getTakeawayPrice();
                    break;
                default:
                    break;
            }

            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            Double totalProdpaiedPrice = Double.valueOf(prodPaiedValue) * prodPrice;
            totalPaidCalculationsOfOrders += totalProdpaiedPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }

        if (table.getDiscount() != null && table.getDiscount() > 0 && table.getDiscount() < 100) {
            totalCalculationsOfOrders = (double) Math.round((100 - table.getDiscount()) * totalCalculationsOfOrders / 100);
        }

        table.setPaidOrdersPrice(totalPaidCalculationsOfOrders);
        table.setTotalPrice(totalCalculationsOfOrders - totalPaidCalculationsOfOrders);
        return tableRepository.save(table);
    }

    private Double calculateNetDeviceSessionOrderesPrice(Table table) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : table.getOrdersData()) {
            int prodValue;
            if (table.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = table.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 1;
            }
            Double prodPrice = order.getPrice();
            switch (table.getType()) {
                case TABLE:
                    prodPrice = order.getPrice();
                    break;
                case SHOPS:
                    prodPrice = order.getShopsPrice();
                    break;
                case TAKEAWAY:
                    prodPrice = order.getTakeawayPrice();
                    break;
                default:
                    break;
            }

            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }
        return totalCalculationsOfOrders;
    }

    public Table payProductfromTableSession(String tableId, String productId) {
        Optional<Table> devFrom = tableRepository.findById(tableId);
        if (!devFrom.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Table sess = devFrom.get();

        Optional<Product> product = productService.findOneDomain(productId);
        if (product.isPresent()) {
            try {
                boolean sessionOrdersHasItem = sess.getOrdersQuantity().containsKey(productId);
                boolean sessionPaidOrdersHasItem = sess.getPaidOrdersQuantity().containsKey(productId);
                int currentOrdersvalue = sess.getOrdersQuantity().get(productId);

                if (sessionOrdersHasItem && currentOrdersvalue > 0) {
                    if (sessionPaidOrdersHasItem) {
                        // update
                        int currentPaidOrdersvalue = sess.getPaidOrdersQuantity().get(productId);

                        if (currentOrdersvalue > currentPaidOrdersvalue) {
                            sess.getPaidOrdersQuantity().put(productId, (currentPaidOrdersvalue + 1));
                        }
                    } else {
                        // add

                        if (currentOrdersvalue >= 1) {
                            sess.getPaidOrdersQuantity().put(productId, 1);
                        }
                    }
                    Table sessSaved = tableRepository.save(sess);
                    this.calculateDeviceSessionOrderesPrice(sessSaved);
                }
            } catch (Exception e) {}
        }

        return tableRepository.findById(tableId).orElse(null);
    }

    public Table unPayProductFromTableSession(String tableId, String productId) {
        Optional<Table> devFrom = tableRepository.findById(tableId);
        if (!devFrom.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Table sess = devFrom.get();

        Optional<Product> product = productService.findOneDomain(productId);
        if (product.isPresent()) {
            try {
                boolean sessionPaidOrdersHasItem = sess.getPaidOrdersQuantity().containsKey(productId);
                int currentOrdersvalue = sess.getOrdersQuantity().get(productId);
                int currentPaidOrdersvalue = sess.getPaidOrdersQuantity().get(productId);

                if (sessionPaidOrdersHasItem && currentPaidOrdersvalue > 0) {
                    // update

                    if (currentPaidOrdersvalue <= 1) {
                        if (currentOrdersvalue >= currentPaidOrdersvalue) {
                            sess.getPaidOrdersQuantity().remove(productId);
                        }
                    } else {
                        if (currentOrdersvalue >= currentPaidOrdersvalue) {
                            sess.getPaidOrdersQuantity().put(productId, (currentPaidOrdersvalue - 1));
                        }
                    }

                    Table sessSaved = tableRepository.save(sess);
                    this.calculateDeviceSessionOrderesPrice(sessSaved);
                    // this.addProductToDeviceSession(deviceId, productId);

                }
            } catch (Exception e) {}
        }

        return tableRepository.findById(tableId).orElse(null);
    }

    public Table completePayDevicePaidOrders(String tableId, boolean print) {
        Optional<Table> devFrom = tableRepository.findById(tableId);
        if (!devFrom.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Table sess = devFrom.get();

        Set<Product> productsList = new HashSet<>();
        for (String product : sess.getPaidOrdersQuantity().keySet()) {
            int currentPaidOrdersvalue = sess.getPaidOrdersQuantity().get(product);
            for (int i = 0; i < currentPaidOrdersvalue; i++) {
                Optional<Product> productX = productService.findOneDomain(product);
                productsList.add(productX.get());
                deleteProductFromDeviceSessionInAction(tableId, productX.get().getId());
            }
        }

        Optional<Table> tableDev = tableRepository.findById(tableId);
        if (tableDev.isPresent()) {
            Table newTabel = new Table();
            newTabel.setActive(false);
            newTabel.setOrdersQuantity(tableDev.get().getPaidOrdersQuantity());
            newTabel.setName(tableDev.get().getName());

            for (Product product : sess.getOrdersData()) {
                for (String productFound : sess.getPaidOrdersQuantity().keySet()) {
                    if (product.getId().equals(productFound)) {
                        newTabel.getOrdersData().add(product);
                        break;
                    }
                }
            }
            newTabel.setType(tableDev.get().getType());
            newTabel.setDiscount(0.0);
            double total = calculateNetDeviceSessionOrderesPrice(newTabel);
            newTabel.setTotalPrice(total);

            TableRecord tableRecord = new TableRecord();
            tableRecord.setDiscount(0.0);

            tableRecord.setNetTotalPrice(total);
            tableRecord.setTotalPrice(total);
            tableRecord.setOrdersData(newTabel.getOrdersData());
            tableRecord.setOrdersQuantity(newTabel.getOrdersQuantity());
            tableRecord.setTable(newTabel);

            tableRecord.setType(newTabel.getType());

            tableRecord.setTotalDiscountPrice(0.0);

            TableRecord SavedTableRecord = recordRepository.save(tableRecord);

            if (print) {
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

            Table toFinishUpdateTable = tableDev.get();

            toFinishUpdateTable.setPaidOrdersPrice(0.0);
            toFinishUpdateTable.setPaidOrdersQuantity(new HashMap<>());
            Table sessSaved = tableRepository.save(toFinishUpdateTable);
            calculateDeviceSessionOrderesPrice(sessSaved);
        }
        return tableRepository.findById(tableId).orElse(null);
    }
}
