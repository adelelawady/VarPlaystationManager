package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.ShopsOrders;
import com.mycompany.myapp.domain.TableRecord;
import com.mycompany.myapp.domain.Takeaway;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.repository.ShopsOrdersRepository;
import com.mycompany.myapp.repository.TableRecordRepository;
import com.mycompany.myapp.repository.TableRepository;
import com.mycompany.myapp.repository.TakeawayRepository;
import com.mycompany.myapp.service.ProductService;
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
public class ReportResource {

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    TableRecordRepository tableRecordRepository;

    @Autowired
    ShopsOrdersRepository shopsOrdersRepository;

    @Autowired
    TakeawayRepository takeawayRepository;

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report/main-report")
    public ResponseEntity<AdminReportDTO> getMainReport(@RequestBody FromToDTO fromToDto) {
        AdminReportDTO adminReport = new AdminReportDTO();

        /////// DEVICE RECORD

        LocalDate localDateFrom = fromToDto.getFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime StartOfDayFrom = localDateFrom.atTime(LocalTime.MIN);
        System.out.println(StartOfDayFrom);

        LocalDate localDateTo = fromToDto.getTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime EndOfDayTo = localDateTo.atTime(LocalTime.MAX);
        System.out.println(EndOfDayTo);
        List<Record> allDevicesrecords =
            this.recordRepository.findAllByStartBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        Double TotalPriceTimeDevices = allDevicesrecords.stream().map(Record::getTotalPriceTime).mapToDouble(Double::doubleValue).sum();

        Double TotalPriceOrdersDevices = allDevicesrecords.stream().map(Record::getTotalPriceOrders).mapToDouble(Double::doubleValue).sum();

        Double TotalPriceUserDevices = allDevicesrecords.stream().map(Record::getTotalPriceUser).mapToDouble(Double::doubleValue).sum();

        Double TotalPriceDevices = TotalPriceTimeDevices + TotalPriceOrdersDevices;

        adminReport.setTotalPriceTimeDevices(TotalPriceTimeDevices);

        adminReport.setTotalPriceOrdersDevices(TotalPriceOrdersDevices);

        adminReport.setTotalPriceDevices(TotalPriceDevices);
        adminReport.setTotalPriceUserDevices(TotalPriceUserDevices);
        List<TableRecord> allTablesrecords =
            this.tableRecordRepository.findAllByCreatedDateBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );
        Double TotalPriceTables = allTablesrecords.stream().map(TableRecord::getTotalPrice).mapToDouble(Double::doubleValue).sum();
        adminReport.setTotalPriceOrdersTables(TotalPriceTables);

        List<ShopsOrders> allTakeAwayrecords =
            this.shopsOrdersRepository.findAllByCreatedDateBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );
        Double TotalPriceShops = allTakeAwayrecords.stream().map(ShopsOrders::getTotalPrice).mapToDouble(Double::doubleValue).sum();
        adminReport.setTotalPriceOrdersShops(TotalPriceShops);

        List<Takeaway> allShopsRecords =
            this.takeawayRepository.findAllByCreatedDateBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        Double TotalPriceTakeAway = allShopsRecords.stream().map(Takeaway::getTotalPrice).mapToDouble(Double::doubleValue).sum();
        adminReport.setTotalPriceOrdersTakeAway(TotalPriceTakeAway);

        return ResponseEntity.ok(adminReport);
    }
}
