package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ReceiptPrint;
import com.mycompany.myapp.service.RecordService;
import com.mycompany.myapp.service.dto.RecordDTO;
import com.mycompany.myapp.service.dto.RecordsFilterDTO;
import com.mycompany.myapp.service.dto.RecordsFilterRequestDTO;
import com.mycompany.myapp.service.mapper.RecordMapper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Service Implementation for managing {@link Record}.
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);

    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

    public RecordServiceImpl(RecordRepository recordRepository, RecordMapper recordMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
    }

    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        log.debug("Request to save Record : {}", recordDTO);
        Record record = recordMapper.toEntity(recordDTO);
        record = recordRepository.save(record);
        return recordMapper.toDto(record);
    }

    @Override
    public Optional<RecordDTO> partialUpdate(RecordDTO recordDTO) {
        log.debug("Request to partially update Record : {}", recordDTO);

        return recordRepository
            .findById(recordDTO.getId())
            .map(existingRecord -> {
                recordMapper.partialUpdate(existingRecord, recordDTO);

                return existingRecord;
            })
            .map(recordRepository::save)
            .map(recordMapper::toDto);
    }

    @Override
    public Page<RecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Records");
        return recordRepository.findAll(pageable).map(recordMapper::toDto);
    }

    public Page<RecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recordRepository.findAllWithEagerRelationships(pageable).map(recordMapper::toDto);
    }

    @Override
    public Optional<RecordDTO> findOne(String id) {
        log.debug("Request to get Record : {}", id);
        return recordRepository.findOneWithEagerRelationships(id).map(recordMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Record : {}", id);
        recordRepository.deleteById(id);
    }

    @Override
    public com.mycompany.myapp.domain.Record save(com.mycompany.myapp.domain.Record record) {
        return this.recordRepository.save(record);
    }

    @Override
    public RecordsFilterDTO findAllFilterd(Pageable pageable, RecordsFilterRequestDTO filterRequestDTO) {
        Page<Record> result = null;

        List<Record> allrecords = null;

        RecordsFilterDTO res = new RecordsFilterDTO();
        switch (filterRequestDTO.getQueryType()) {
            case "today":
                LocalDate localDate = LocalDate.now();

                LocalDateTime EndOfDay = localDate.atTime(LocalTime.MAX);

                LocalDateTime StartOfDay = localDate.atTime(LocalTime.MIN);

                System.out.println(EndOfDay);
                System.out.println(StartOfDay);

                result =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            pageable,
                            StartOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDay.atZone(ZoneId.systemDefault()).toInstant()
                        );

                allrecords =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            StartOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDay.atZone(ZoneId.systemDefault()).toInstant()
                        );

                break;
            case "yesterday":
                LocalDate localDateyesterday = LocalDate.now().minusDays(1);

                LocalDateTime EndOfDayyesterday = localDateyesterday.atTime(LocalTime.MAX);

                LocalDateTime StartOfDayyesterday = localDateyesterday.atTime(LocalTime.MIN);

                result =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            pageable,
                            StartOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant()
                        );

                allrecords =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            StartOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterday.atZone(ZoneId.systemDefault()).toInstant()
                        );

                break;
            case "lastweek":
                LocalDate localDateyesterdayx = LocalDate.now().minusWeeks(1);

                LocalDate localDateyesterdayx1 = LocalDate.now();

                LocalDateTime EndOfDayyesterdayx = localDateyesterdayx1.atTime(LocalTime.MAX);

                LocalDateTime StartOfDayyesterdayx = localDateyesterdayx.atTime(LocalTime.MIN);

                System.out.println(EndOfDayyesterdayx);
                System.out.println(StartOfDayyesterdayx);
                result =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            pageable,
                            StartOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant()
                        );

                allrecords =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            StartOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDayyesterdayx.atZone(ZoneId.systemDefault()).toInstant()
                        );

                break;
            case "lastmonth":
                LocalDate localDatemonth = LocalDate.now().minusMonths(1);

                LocalDate localDatemonth1 = LocalDate.now();

                LocalDateTime EndOfDaymonth = localDatemonth1.atTime(LocalTime.MAX);

                LocalDateTime StartOfDayMonth = localDatemonth.atTime(LocalTime.MIN);

                result =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            pageable,
                            StartOfDayMonth.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDaymonth.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayMonth.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDaymonth.atZone(ZoneId.systemDefault()).toInstant()
                        );

                allrecords =
                    this.recordRepository.findAllByStartBetweenOrEndBetween(
                            StartOfDayMonth.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDaymonth.atZone(ZoneId.systemDefault()).toInstant(),
                            StartOfDayMonth.atZone(ZoneId.systemDefault()).toInstant(),
                            EndOfDaymonth.atZone(ZoneId.systemDefault()).toInstant()
                        );

                break;
            default:
                break;
        }

        for (Record reco : allrecords) {
            res.setTotalPriceUser(res.getTotalPriceUser() + reco.getTotalPriceUser());
            res.setTotalPrice(res.getTotalPrice() + reco.getTotalPrice());
            res.setTotalPriceOrders(res.getTotalPriceOrders() + reco.getTotalPriceOrders());

            res.setTotalPriceTime(res.getTotalPriceTime() + reco.getTotalPriceTime());
            res.setTotalHours(res.getTotalHours() + reco.getDuration().toHoursPart());
            res.setTotalMinutes(res.getTotalMinutes() + reco.getDuration().toMinutesPart());
        }

        res.setResultList(result.map(recordMapper::toDto));

        // TODO Auto-generated method stub
        return res;
    }

    @Override
    public Optional<Record> findOneDomain(String id) {
        return recordRepository.findById(id);
    }

    public void printRecord(String recId) {
        Optional<com.mycompany.myapp.domain.Record> record = findOneDomain(recId);

        if (!record.isPresent()) {
            return;
        }
        Printable printable = new ReceiptPrint(record.get());

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
