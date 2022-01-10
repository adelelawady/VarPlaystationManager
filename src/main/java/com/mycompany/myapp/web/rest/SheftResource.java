package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Sheft;
import com.mycompany.myapp.domain.ShopsOrders;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.domain.Takeaway;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.repository.SheftRepository;
import com.mycompany.myapp.repository.ShopsOrdersRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.repository.TableRepository;
import com.mycompany.myapp.repository.TakeawayRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.AdminReportDTO;
import com.mycompany.myapp.service.dto.FromToDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        if (sheftRepository.findAllByEnd(null).isEmpty()) {
            return null;
        } else {
            activeSheft = sheftRepository.findAllByEnd(null).get(0);
            activeSheft.setEnd(Instant.now());
            activeSheft = sheftRepository.save(activeSheft);
            stopSheft(activeSheft.getId());
        }

        sheftRepository
            .findAllByEnd(null)
            .stream()
            .forEach(sheft -> {
                sheft.setEnd(Instant.now());
                sheftRepository.save(sheft);
            });
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

            Double total_net_price_devices = 0.0;

            //records.stream().map(Record::getTotalPrice)
            //.mapToDouble(Double::doubleValue).sum();

            Double total_net_user_price_devices = records.stream().map(Record::getTotalPriceUser).mapToDouble(Double::doubleValue).sum();

            Double total_discount_price_devices = records
                .stream()
                .map(Record::getTotalDiscountPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

            Double total_price_time_devices = records.stream().map(Record::getTotalPriceTime).mapToDouble(Double::doubleValue).sum();

            Double total_price_orders_devices = records.stream().map(Record::getTotalPriceOrders).mapToDouble(Double::doubleValue).sum();

            total_net_price_devices = (total_price_time_devices + total_price_orders_devices) - total_discount_price_devices;

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
                total_net_price_devices +
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
}
