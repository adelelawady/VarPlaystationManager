package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.PlaygroundRecoredRepository;
import com.mycompany.myapp.repository.PlaygroundRepository;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
public class PlayGroundServiceImpl implements PlayGroundService {

    private final Logger log = LoggerFactory.getLogger(PlayGroundServiceImpl.class);

    private final PlaygroundRepository deviceRepository;

    //private final DeviceMapper deviceMapper;

    private final SessionService sessionService;

    private final PlaygroundRecoredRepository playgroundRecoredRepository;

    private final ProductService productService;

    public PlayGroundServiceImpl(
        //DeviceRepository deviceRepository,
        //DeviceMapper deviceMapper,
        PlaygroundRepository deviceRepository,
        SessionService sessionService,
        PlaygroundRecoredRepository playgroundRecoredRepository,
        ProductService productService
    ) {
        this.deviceRepository = deviceRepository;
        //this.deviceRepository = deviceRepository;
        //this.deviceMapper = deviceMapper;
        this.sessionService = sessionService;
        this.playgroundRecoredRepository = playgroundRecoredRepository;
        this.productService = productService;
    }

    @Override
    public Playground save(Playground deviceDTO) {
        return deviceRepository.save(deviceDTO);
    }

    @Override
    public Page<Playground> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    @Override
    public Optional<Playground> findOne(String id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }

    @Override
    public Session getDeviceActiveSession(String deviceId) {
        Optional<Playground> deviceOp = deviceRepository.findById(deviceId);
        if (!deviceOp.isPresent() || deviceOp.get().getSession() == null) {
            return null;
        }
        return deviceOp.get().getSession();
    }

    private Playground toDeviceSession(String deviceId) {
        Optional<Playground> deviceOp = deviceRepository.findById(deviceId);

        if (!deviceOp.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }

        Playground device = deviceOp.get();

        for (PlaygroundTime time : device.getPlaygroundTimeList()) {
            Instant now = Instant.now();

            LocalDate localDateNow = now.atZone(ZoneId.systemDefault()).toLocalDate();
            Date dNow = Date.from(time.getFrom());
            java.time.LocalDate dateNow = LocalDate.now();
            java.time.LocalTime dateTimeNow = java.time.LocalTime.of(dNow.getHours(), dNow.getMinutes());
            LocalDateTime StartOfDayNow = LocalDateTime.of(dateNow, dateTimeNow);

            LocalDate localDateFrom = time.getFrom().atZone(ZoneId.systemDefault()).toLocalDate();
            Date d = Date.from(time.getFrom());
            java.time.LocalDate dateFrom = LocalDate.now();
            java.time.LocalTime dateTimeFrom = java.time.LocalTime.of(d.getHours(), d.getMinutes());
            LocalDateTime StartOfDayFrom = LocalDateTime.of(dateNow, dateTimeFrom);

            LocalDate localDateTo = time.getTo().atZone(ZoneId.systemDefault()).toLocalDate();
            Date dTo = Date.from(time.getFrom());
            java.time.LocalTime dateTimeNowTo = java.time.LocalTime.of(dTo.getHours(), dTo.getMinutes());
            LocalDateTime StartOfDayTo = LocalDateTime.of(dateNow, dateTimeNowTo);

            System.out.println(StartOfDayFrom);
            System.out.println(StartOfDayNow);

            System.out.println(StartOfDayFrom.isAfter(StartOfDayNow));
            System.out.println(StartOfDayTo.isBefore(StartOfDayNow));

            if (StartOfDayFrom.isAfter(StartOfDayNow)) {
                time.setActive(true);
            }
        }

        return device;
    }

    @Override
    public List<Playground> devicesWithSessions() {
        return this.deviceRepository.findAll().stream().map(device -> this.toDeviceSession(device.getId())).collect(Collectors.toList());
    }

    @Override
    public Playground startSession(String deviceId, SessionStartDTO sessionStart) {
        Optional<Playground> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session currentActiveSession = getDeviceActiveSession(deviceId);
        if (currentActiveSession == null) {
            Session session = new Session();
            session.setActive(true);
            // session.setDevice(dev.get());
            session.setDeviceId(dev.get().getId());
            session.setDeviceName(dev.get().getName());
            session.setStart(Instant.now().minusSeconds((sessionStart.getPlusMinutes() * 60)));
            session.setMulti(sessionStart.isMulti());
            session.reserved(sessionStart.getReserved());
            sessionService.save(session);
            Playground device = dev.get();
            device.setSession(session);
            deviceRepository.save(device);
        }

        return this.toDeviceSession(deviceId);
    }

    private void calculateRecoredTimePrice(Playground dev, PlaygroundRecord rec, Session currentActiveSession) {
        Double totalPriceCalculatedTime;

        Duration d = Duration.between(currentActiveSession.getStart(), Instant.now());

        int minutes = (int) d.toMinutes();
        Double houres = Double.valueOf((double) minutes / 60.0);

        totalPriceCalculatedTime = houres * dev.getPrice();

        totalPriceCalculatedTime = (double) Math.round(totalPriceCalculatedTime);

        rec.setTotalPriceTime(totalPriceCalculatedTime);
    }

    private void calculateRecoredNetPrice(PlaygroundRecord rec) {
        Double totalNetPriceTimeCalculated = rec.getTotalPriceTime() + rec.getTotalPriceOrders() + rec.getPreviousSessionsTotalPrice();

        // RECORED NET PRICE
        rec.setTotalNetPriceCalculated(totalNetPriceTimeCalculated);
    }

    private void calculateRecoredTotalPriceDiscount(PlaygroundRecord rec, Session currentActiveSession, SessionEndDTO sessionEndDto) {
        Double totalPriceCalculated = 0.0;
        Double totalPriceOrdersCalculated = 0.0;
        Double totalPriceTimeCalculated = 0.0;

        totalPriceOrdersCalculated = rec.getTotalPriceOrders();
        // }

        totalPriceTimeCalculated = rec.getTotalPriceTime();
        // }

        totalPriceCalculated = totalPriceOrdersCalculated + totalPriceTimeCalculated;

        //totalPriceCalculated += currentActiveSession.getPreviousSessionsTotalPrice();
        // RECORED TOTALPRICE
        rec.setTotalPrice(totalPriceCalculated);
    }

    PlaygroundRecord stopSession(Playground dev, Session currentActiveSession, SessionEndDTO sessionEndDto, boolean save) {
        // STOP DEVICE ACTIVE SESSION AND SAVE
        currentActiveSession.setActive(false);
        sessionService.save(currentActiveSession);

        // CREATE NEW RECORED
        PlaygroundRecord rec = new PlaygroundRecord();
        // RECORED -- END
        rec.setEnd(Instant.now());
        // RECORED START
        rec.setStart(currentActiveSession.getStart());

        // RECORED DURATION
        Duration d = Duration.between(currentActiveSession.getStart(), Instant.now());
        rec.setDuration(d);

        // RECORED -- USER INPUT PRICE
        rec.setTotalPriceUser(sessionEndDto.getTotalPrice());

        // DEVICE TIME
        calculateRecoredTimePrice(dev, rec, currentActiveSession);

        // DEVICE ORDERS
        rec.setTotalPriceOrders(currentActiveSession.getOrdersPrice());

        // RECORED NET PRICE
        calculateRecoredNetPrice(rec);

        // RECORED TOTALPRICE & DISCOUNT
        calculateRecoredTotalPriceDiscount(rec, currentActiveSession, sessionEndDto);

        // RECORED ORDERS
        if (currentActiveSession.getOrders() != null && !currentActiveSession.getOrders().isEmpty()) {
            rec.setOrdersQuantity(currentActiveSession.getOrdersQuantity());
            rec.setOrdersData(currentActiveSession.getOrders());
        }

        if (save) {
            return playgroundRecoredRepository.save(rec);
        } else {
            return rec;
        }
    }

    @Override
    public Playground stopSession(String deviceId, SessionEndDTO sessionEndDto) {
        try {
            Optional<Playground> dev = deviceRepository.findById(deviceId);
            if (!dev.isPresent()) {
                throw new RuntimeException("DeviceNotFound");
            }
            PlaygroundRecord savedRecId = null;
            Session currentActiveSession = getDeviceActiveSession(deviceId);
            if (currentActiveSession != null) {
                savedRecId = stopSession(dev.get(), currentActiveSession, sessionEndDto, true);
                Playground device = dev.get();
                device.setSession(null);
                deviceRepository.save(device);
                if (sessionEndDto.isPrint() && savedRecId != null) {
                    printRecord(savedRecId.getId());
                }
            }

            this.sessionService.stopAllDeviceActiveSessions(deviceId);
        } catch (Exception e) {
            this.toDeviceSession(deviceId);
        }
        return this.toDeviceSession(deviceId);
    }

    public void printRecord(String recId) {
        Optional<PlaygroundRecord> record = playgroundRecoredRepository.findById(recId);

        if (!record.isPresent()) {
            return;
        }
        Printable printable = new ReceiptPlayGroundPrint(record.get());

        PrinterSupport ps = new PrinterSupport();

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(printable, ps.getPageFormat(pj));

        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Playground deviceWithSession(String deviceId) {
        return this.toDeviceSession(deviceId);
    }

    @Override
    public Playground addProductToDeviceSession(String deviceId, String productId) {
        Optional<Playground> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sess = getDeviceActiveSession(deviceId);

        if (sess != null) {
            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                sessionService.addProductOrderToDeviceSession(sess, product.get());

                Session savedSession = getDeviceActiveSession(deviceId);
                savedSession.addOrders(product.get());
                Session savedSession2 = sessionService.save(savedSession);
                sessionService.calculateDeviceSessionOrderesPrice(savedSession2);
            }
        }

        return this.toDeviceSession(deviceId);
    }

    @Override
    public Playground deleteProductFromDeviceSession(String deviceId, String productId) {
        Optional<Playground> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sess = getDeviceActiveSession(deviceId);

        if (sess != null) {
            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                sessionService.deleteProductOrderFromDeviceSession(sess, product.get());

                Session savedSession = getDeviceActiveSession(deviceId);
                try {
                    boolean sessionHasItem = savedSession.getOrdersQuantity().containsKey(productId);
                    if (sessionHasItem) {
                        int currentvalue = savedSession.getOrdersQuantity().get(productId);
                        if (currentvalue == 0) {
                            boolean sessionPaidHasItem = savedSession.getPaidOrdersQuantity().containsKey(productId);
                            if (!sessionPaidHasItem) {
                                savedSession.removeOrders(product.get());
                            }
                        }
                    } else {
                        boolean sessionPaidHasItem = savedSession.getPaidOrdersQuantity().containsKey(productId);
                        if (!sessionPaidHasItem) {
                            savedSession.removeOrders(product.get());
                        }
                    }
                } catch (Exception e) {}

                Session savedSession2 = sessionService.save(savedSession);
                sessionService.calculateDeviceSessionOrderesPrice(savedSession2);
            }
        }

        return this.toDeviceSession(deviceId);
    }

    Session addProductsToSession(Session sess, Session sess2) {
        // add all missing products
        sess.getOrders().addAll(sess2.getOrders());

        for (Product prodToAdd : sess2.getOrders()) {
            boolean sessionHasItem = sess.getOrdersQuantity().containsKey(prodToAdd.getId());
            int currentvalueTable = sess2.getOrdersQuantity().get(prodToAdd.getId());
            if (sessionHasItem) {
                int currentvalueDevice = sess.getOrdersQuantity().get(prodToAdd.getId());
                sess.getOrdersQuantity().put(prodToAdd.getId(), currentvalueDevice + currentvalueTable);
            } else {
                sess.getOrdersQuantity().put(prodToAdd.getId(), currentvalueTable);
            }
        }
        sessionService.calculateDeviceSessionOrderesPrice(sess);
        Optional<Session> sessionSaved = sessionService.findById(sess.getId());
        if (sessionSaved.isPresent()) {
            return sessionSaved.get();
        }
        return null;
    }
}
