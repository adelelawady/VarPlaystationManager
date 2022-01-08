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
            return ResponseEntity.ok(sheftRepository.findAllByEnd(null).get(0));
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

            List<Record> records = recordRepository.findAllByEndBetween(sheftFound.get().getStart(), sheftFound.get().getEnd());
            sheftObj.setRecords(records);

            List<TableRecord> tableRecords = tableRecordRepository.findAllByCreatedDateBetween(
                sheftFound.get().getStart(),
                sheftFound.get().getEnd()
            );
            sheftObj.setTableRecords(tableRecords);

            Double total_net_price = records.stream().map(Record::getTotalPrice).mapToDouble(Double::doubleValue).sum();

            Double total_discount = records.stream().map(Record::getTotalPrice).mapToDouble(Double::doubleValue).sum();

            /* @Field("total_net_price_after_discount")
	        private Double total_net_price_after_discount=0.0;

	        
	        
	        
	        @Field("total_net_price_devices")
	        private Double total_net_price_devices=0.0;
	        
	        @Field("total_net_user_price_devices")
	        private Double total_net_user_price_devices=0.0;
	        
	        @Field("total_discount_price_devices")
	        private Double total_discount_price_devices=0.0;
	        
	        
	        @Field("total_price_time_devices")
	        private Double total_discount_price_time_devices=0.0;
	        
	        
	        
	        @Field("total_price_orders_devices")
	        private Double total_discount_price_orders_devices=0.0;
	        
	        
	        
	        
	        
	        
	        @Field("total_net_price_after_discount_devices")
	        private Double total_net_price_after_discount_devices=0.0;
	        
	        
	        
	        
	        @Field("total_net_price_Tables")
	        private Double total_net_price_Tables=0.0;
	        
	        
	        @Field("total_discount_price_Tables")
	        private Double total_discount_price_Tables=0.0;
	        
	        
	        @Field("total_net_price_after_discount_Tables")
	        private Double total_net_price_after_discount_Tables=0.0;
	        
	        
	        
	        
	        @Field("total_net_price_takeaway")
	        private Double total_net_price_takeaway=0.0;
	        
	        
	        @Field("total_discount_price_takeaway")
	        private Double total_discount_price_takeaway=0.0;
	        
	        
	        @Field("total_net_price_after_discount_takeaway")
	        private Double total_net_price_after_discount_takeaway=0.0;
	        
	        
	        
	        
	        @Field("total_net_price_shops")
	        private Double total_net_price_shops=0.0;
	        
	        
	        @Field("total_discount_price_shops")
	        private Double total_discount_price_shops=0.0;
	        
	        
	        @Field("total_net_price_after_discount_shops")
	        private Double total_net_price_after_discount_shops=0.0;*/

            sheftRepository.save(sheftObj);
        }
    }
}
