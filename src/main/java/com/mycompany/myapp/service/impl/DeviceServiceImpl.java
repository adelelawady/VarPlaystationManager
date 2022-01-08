package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.domain.Table;
import com.mycompany.myapp.repository.DeviceRepository;
import com.mycompany.myapp.service.DeviceService;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.RecordService;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.dto.SessionEndDTO;
import com.mycompany.myapp.service.dto.SessionStartDTO;
import com.mycompany.myapp.service.mapper.DeviceMapper;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.websocket.RemoteEndpoint.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    private final SessionService sessionService;

    private final RecordService recordService;

    private final ProductService productService;

    public DeviceServiceImpl(
        DeviceRepository deviceRepository,
        DeviceMapper deviceMapper,
        SessionService sessionService,
        RecordService recordService,
        ProductService productService
    ) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.sessionService = sessionService;
        this.recordService = recordService;
        this.productService = productService;
    }

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    public Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO) {
        log.debug("Request to partially update Device : {}", deviceDTO);

        return deviceRepository
            .findById(deviceDTO.getId())
            .map(existingDevice -> {
                deviceMapper.partialUpdate(existingDevice, deviceDTO);

                return existingDevice;
            })
            .map(deviceRepository::save)
            .map(deviceMapper::toDto);
    }

    @Override
    public Page<DeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable).map(deviceMapper::toDto);
    }

    @Override
    public Optional<DeviceDTO> findOne(String id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }

    private DeviceSessionDTO toDeviceSession(String deviceId) {
        Optional<Device> deviceOp = deviceRepository.findById(deviceId);

        if (!deviceOp.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }

        Device device = deviceOp.get();
        DeviceSessionDTO dev = new DeviceSessionDTO();
        dev.setId(device.getId());
        if (device.getType() != null) {
            dev.setType(device.getType().getName());
        }
        dev.setName(device.getName());

        Session sess = sessionService.getDeviceActiveSession(device.getId());

        if (sess != null) {
            dev.setSession(sess);
        }
        return dev;
    }

    @Override
    public List<DeviceSessionDTO> devicesWithSessions() {
        return this.deviceRepository.findAll().stream().map(device -> this.toDeviceSession(device.getId())).collect(Collectors.toList());
    }

    @Override
    public DeviceSessionDTO startSession(String deviceId, SessionStartDTO sessionStart) {
        Optional<Device> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session currentActiveSession = sessionService.getDeviceActiveSession(deviceId);
        if (currentActiveSession == null) {
            Session session = new Session();
            session.setActive(true);
            session.setDevice(dev.get());

            session.setStart(Instant.now().minusSeconds((sessionStart.getPlusMinutes() * 60)));
            session.setMulti(sessionStart.isMulti());
            session.reserved(sessionStart.getReserved());
            sessionService.save(session);
        }

        return this.toDeviceSession(deviceId);
    }

    Record stopSession(Device dev, Session currentActiveSession, SessionEndDTO sessionEndDto, boolean save) {
        currentActiveSession.setActive(false);
        sessionService.save(currentActiveSession);

        Record rec = new Record();
        rec.setDevice(dev);
        rec.setEnd(Instant.now());
        rec.setTotalPriceUser(sessionEndDto.getTotalPrice());

        Double totalPriceCalculatedTime;

        Duration d = Duration.between(currentActiveSession.getStart(), Instant.now());

        int minutes = (int) d.toMinutes();
        Double houres = Double.valueOf((double) minutes / 60.0);

        if (currentActiveSession.isMulti()) {
            totalPriceCalculatedTime = houres * currentActiveSession.getDevice().getType().getPricePerHourMulti();
        } else {
            totalPriceCalculatedTime = houres * currentActiveSession.getDevice().getType().getPricePerHour();
        }

        totalPriceCalculatedTime = (double) Math.round(totalPriceCalculatedTime);

        // Double totalOrderPriceAfterDiscount

        rec.setTotalPriceTime(totalPriceCalculatedTime);

        rec.setTotalPriceOrders(currentActiveSession.getOrdersPrice());
        rec.setMulti(currentActiveSession.isMulti());

        Double totalPriceCalculated = 0.0;
        Double totalPriceOrdersCalculated = 0.0;
        Double totalPriceTimeCalculated = 0.0;

        if (sessionEndDto.getOrdersDiscount() > 0 && sessionEndDto.getOrdersDiscount() < 100) {
            totalPriceOrdersCalculated = (double) Math.round((100 - sessionEndDto.getOrdersDiscount()) * rec.getTotalPriceOrders() / 100);
            rec.setOrdersDiscount(sessionEndDto.getOrdersDiscount());
        } else {
            totalPriceOrdersCalculated = rec.getTotalPriceOrders();
        }

        if (sessionEndDto.getTimeDiscount() > 0 && sessionEndDto.getTimeDiscount() < 100) {
            totalPriceTimeCalculated = (double) Math.round((100 - sessionEndDto.getTimeDiscount()) * rec.getTotalPriceTime() / 100);
            rec.setTimeDiscount(sessionEndDto.getTimeDiscount());
        } else {
            totalPriceTimeCalculated = rec.getTotalPriceTime();
        }

        totalPriceCalculated = totalPriceOrdersCalculated + totalPriceTimeCalculated;

        totalPriceCalculated += currentActiveSession.getPreviousSessionsTotalPrice();

        rec.setTotalPrice(totalPriceCalculated);

        rec.setDuration(d);
        if (currentActiveSession.getOrders() != null && !currentActiveSession.getOrders().isEmpty()) {
            rec.setOrdersQuantity(currentActiveSession.getOrdersQuantity());
            rec.setOrdersData(currentActiveSession.getOrders());
        }

        rec.setStart(currentActiveSession.getStart());
        if (save) {
            if (!currentActiveSession.getPreviousSessions().isEmpty()) {
                rec.setPreviousSessionsTotalPrice(currentActiveSession.getPreviousSessionsTotalPrice());
                rec.setPreviousSessions(currentActiveSession.getPreviousSessions());
            }
            return recordService.save(rec);
        } else {
            return rec;
        }
    }

    @Override
    public DeviceSessionDTO stopSession(String deviceId, SessionEndDTO sessionEndDto) {
        this.sessionService.stopAllDeviceActiveSessions(deviceId);

        try {
            Optional<Device> dev = deviceRepository.findById(deviceId);
            if (!dev.isPresent()) {
                throw new RuntimeException("DeviceNotFound");
            }
            Record savedRecId = null;
            Session currentActiveSession = sessionService.getDeviceActiveSession(deviceId);
            if (currentActiveSession != null) {
                savedRecId = stopSession(dev.get(), currentActiveSession, sessionEndDto, true);
            }
            //if (sessionEndDto.isPrint() && savedRecId != null) {
            //	recordService.printRecord(savedRecId.getId());
            //}

        } catch (Exception e) {
            this.toDeviceSession(deviceId);
        }
        return this.toDeviceSession(deviceId);
    }

    @Override
    public DeviceSessionDTO deviceWithSession(String deviceId) {
        return this.toDeviceSession(deviceId);
    }

    @Override
    public DeviceSessionDTO addProductToDeviceSession(String deviceId, String productId) {
        Optional<Device> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sess = sessionService.getDeviceActiveSession(deviceId);

        if (sess != null) {
            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                sessionService.addProductOrderToDeviceSession(sess, product.get());

                Session savedSession = sessionService.getDeviceActiveSession(deviceId);
                savedSession.addOrders(product.get());
                Session savedSession2 = sessionService.save(savedSession);
                sessionService.calculateDeviceSessionOrderesPrice(savedSession2);
            }
        }

        return this.toDeviceSession(deviceId);
    }

    @Override
    public DeviceSessionDTO deleteProductFromDeviceSession(String deviceId, String productId) {
        Optional<Device> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sess = sessionService.getDeviceActiveSession(deviceId);

        if (sess != null) {
            Optional<Product> product = productService.findOneDomain(productId);
            if (product.isPresent()) {
                sessionService.deleteProductOrderFromDeviceSession(sess, product.get());

                Session savedSession = sessionService.getDeviceActiveSession(deviceId);
                try {
                    boolean sessionHasItem = savedSession.getOrdersQuantity().containsKey(productId);
                    if (sessionHasItem) {
                        int currentvalue = savedSession.getOrdersQuantity().get(productId);
                        if (currentvalue == 0) {
                            savedSession.removeOrders(product.get());
                        }
                    } else {
                        savedSession.removeOrders(product.get());
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

        return sessionService.getDeviceActiveSession(sess.getDevice().getId());
    }

    @Override
    public DeviceSessionDTO moveDevice(String moveFromDeviceId, String moveToDeviceId) {
        Optional<Device> devFrom = deviceRepository.findById(moveFromDeviceId);
        Optional<Device> devTo = deviceRepository.findById(moveToDeviceId);
        if (!devFrom.isPresent() || !devTo.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sessFrom = sessionService.getDeviceActiveSession(moveFromDeviceId);
        Session sessTo = sessionService.getDeviceActiveSession(moveToDeviceId);

        if (sessTo != null) {
            throw new RuntimeException("Device is already active");
        }

        if (sessFrom == null) {
            throw new RuntimeException("Device is not active");
        }

        SessionEndDTO sessionEnd = new SessionEndDTO();
        sessionEnd.setOrdersDiscount(0.0);
        sessionEnd.setPrint(false);
        sessionEnd.setTimeDiscount(0.0);
        sessionEnd.setTotalPrice(0.0);
        Record record = stopSession(devFrom.get(), sessFrom, sessionEnd, false);

        sessFrom.setDevice(devTo.get());
        sessFrom.getPreviousSessions().add(record);
        Double currentPreviousSessionsTotalPrice = sessFrom.getPreviousSessionsTotalPrice();
        sessFrom.setActive(true);
        sessFrom.setStart(Instant.now());
        sessFrom.setPreviousSessionsTotalPrice(currentPreviousSessionsTotalPrice + record.getTotalPriceTime());
        sessFrom.setMulti(false);
        sessionService.save(sessFrom);

        return this.toDeviceSession(moveToDeviceId);
    }

    public DeviceSessionDTO moveDeviceMulti(String moveFromDeviceId, boolean multi) {
        Optional<Device> devFrom = deviceRepository.findById(moveFromDeviceId);
        if (!devFrom.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session sessFrom = sessionService.getDeviceActiveSession(moveFromDeviceId);

        if (sessFrom == null) {
            throw new RuntimeException("Device is not active");
        }

        SessionEndDTO sessionEnd = new SessionEndDTO();
        sessionEnd.setOrdersDiscount(0.0);
        sessionEnd.setPrint(false);
        sessionEnd.setTimeDiscount(0.0);
        sessionEnd.setTotalPrice(0.0);
        Record record = stopSession(devFrom.get(), sessFrom, sessionEnd, false);

        sessFrom.setDevice(devFrom.get());

        Double currentPreviousSessionsTotalPrice = sessFrom.getPreviousSessionsTotalPrice();
        sessFrom.setActive(true);
        sessFrom.setStart(Instant.now());

        if (record.getTotalPriceTime() > 0) {
            sessFrom.getPreviousSessions().add(record);
            sessFrom.setPreviousSessionsTotalPrice(currentPreviousSessionsTotalPrice + record.getTotalPriceTime());
        }
        sessFrom.setMulti(multi);
        sessionService.save(sessFrom);

        return this.toDeviceSession(moveFromDeviceId);
    }

    public List<DeviceDTO> findAllByActive(boolean active) {
        return sessionService.findByActive(active).stream().map(Session::getDevice).map(deviceMapper::toDto).collect(Collectors.toList());
    }
}
