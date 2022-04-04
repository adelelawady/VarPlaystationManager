package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.dto.AdminReportDTO;
import com.mycompany.myapp.service.dto.FromToDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
    PlaygroundRecoredRepository playgroundRepository;

    @Autowired
    TableRecordRepository tableRecordRepository;

    @Autowired
    ShopsOrdersRepository shopsOrdersRepository;

    @Autowired
    TakeawayRepository takeawayRepository;

    @Autowired
    PosOrderRecoredRepository posOrderRecoredRepository;

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    private static String convertMongoDateStart(int year, int month, int day, int hour, int minute) {
        return year + "-" + month + "-" + day + " " + "00:0:1.123Z";
    }

    private static String convertMongoDateEnd(int year, int month, int day, int hour, int minute) {
        return year + "-" + month + "-" + day + " " + "23:59:15.123Z";
    }

    @PostMapping("/report/main-report")
    public ResponseEntity<AdminReportDTO> getMainReport(@RequestBody FromToDTO fromToDto) {
        AdminReportDTO adminReport = new AdminReportDTO();

        /////// DEVICE RECORD

        LocalDate localDateFrom = fromToDto.getFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime StartOfDayFrom = localDateFrom.atTime(LocalTime.MIN);
        System.out.println(
            convertMongoDateStart(
                StartOfDayFrom.getYear(),
                StartOfDayFrom.getMonthValue(),
                StartOfDayFrom.getDayOfMonth(),
                StartOfDayFrom.getHour(),
                StartOfDayFrom.getMinute()
            )
        );

        LocalDate localDateTo = fromToDto.getTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime EndOfDayTo = localDateTo.atTime(LocalTime.MAX);
        System.out.println(
            convertMongoDateEnd(
                EndOfDayTo.getYear(),
                EndOfDayTo.getMonthValue(),
                EndOfDayTo.getDayOfMonth(),
                EndOfDayTo.getHour(),
                EndOfDayTo.getMinute()
            )
        );

        /*
       Long total= posOrderRecoredRepository.sumNetTotalByDate(
           convertMongoDateStart(StartOfDayFrom.getYear(),StartOfDayFrom.getMonthValue(),StartOfDayFrom.getDayOfMonth(),
               StartOfDayFrom.getHour(),StartOfDayFrom.getMinute())



           ,


           convertMongoDateEnd(EndOfDayTo.getYear(),EndOfDayTo.getMonthValue(),EndOfDayTo.getDayOfMonth(),
               EndOfDayTo.getHour(),EndOfDayTo.getMinute())

           );

*/

        List<PosOrderRecord> allPosOrderRecCafe =
            this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                    "CAFE",
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        List<PosOrderRecord> allPosOrderRecMarket =
            this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                    "MARKET",
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        List<PosOrderRecord> allPosOrderRecRes =
            this.posOrderRecoredRepository.findAllByTypeAndCreatedDateBetween(
                    "RES",
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        List<Record> allDevicesrecords =
            this.recordRepository.findAllByStartBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        List<PlaygroundRecord> allPlayGroundsrecords =
            this.playgroundRepository.findAllByStartBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        List<TableRecord> allTablesrecords =
            this.tableRecordRepository.findAllByCreatedDateBetween(
                    StartOfDayFrom.atZone(ZoneId.systemDefault()).toInstant(),
                    EndOfDayTo.atZone(ZoneId.systemDefault()).toInstant()
                );

        //Double TotalPriceUserDevices = allDevicesrecords.stream().map(Record::getTotalPriceUser).mapToDouble(Double::doubleValue).sum();
        //Double TotalPriceDevices = allDevicesrecords.stream().map(Record::getTotalPrice).mapToDouble(Double::doubleValue).sum();
        Double TotalPriceDevicesTime = allDevicesrecords.stream().map(Record::getTotalPriceTime).mapToDouble(Double::doubleValue).sum();

        Double TotalPricePlayGroundTime = allPlayGroundsrecords
            .stream()
            .map(PlaygroundRecord::getTotalPriceTime)
            .mapToDouble(Double::doubleValue)
            .sum();

        Double TotalPriceCafe = allPosOrderRecCafe.stream().map(PosOrderRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();
        Double TotalPriceMarket = allPosOrderRecMarket
            .stream()
            .map(PosOrderRecord::getNetTotalPrice)
            .mapToDouble(Double::doubleValue)
            .sum();
        Double TotalPriceRes = allPosOrderRecRes.stream().map(PosOrderRecord::getNetTotalPrice).mapToDouble(Double::doubleValue).sum();

        Double TotalPriceTables = allTablesrecords.stream().map(TableRecord::getTotalPrice).mapToDouble(Double::doubleValue).sum();

        adminReport.setTotalCafe(TotalPriceCafe);
        adminReport.setTotalMarket(TotalPriceMarket);
        adminReport.setTotalRes(TotalPriceRes);
        adminReport.setTotalPriceTimeDevices(TotalPriceDevicesTime);

        //adminReport.setTotalPriceDevices(TotalPriceDevices);
        //adminReport.setTotalPriceUserDevices(TotalPriceUserDevices);
        adminReport.setTotalPriceOrdersTables(TotalPriceTables);
        adminReport.setTotalPlayGround(TotalPricePlayGroundTime);

        adminReport.setTotalPrice(
            TotalPriceCafe +
            TotalPriceMarket +
            TotalPriceRes +
            TotalPriceDevicesTime +
            //+TotalPriceTables
            TotalPricePlayGroundTime
        );

        return ResponseEntity.ok(adminReport);
    }
}
