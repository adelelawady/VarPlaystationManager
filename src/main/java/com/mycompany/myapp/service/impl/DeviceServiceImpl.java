package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.repository.DeviceRepository;
import com.mycompany.myapp.service.DeviceService;
import com.mycompany.myapp.service.RecordService;
import com.mycompany.myapp.service.SessionService;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import com.mycompany.myapp.service.mapper.DeviceMapper;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    private final SessionService sessionService;

    private final RecordService recordService;

    public DeviceServiceImpl(
        DeviceRepository deviceRepository,
        DeviceMapper deviceMapper,
        SessionService sessionService,
        RecordService recordService
    ) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.sessionService = sessionService;
        this.recordService = recordService;
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
        dev.setType(device.getType().getName());
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
    public DeviceSessionDTO startSession(String deviceId) {
        Optional<Device> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session currentActiveSession = sessionService.getDeviceActiveSession(deviceId);
        if (currentActiveSession == null) {
            Session session = new Session();
            session.setActive(true);
            session.setDevice(dev.get());
            session.setStart(Instant.now());
            sessionService.save(session);
        }

        return this.toDeviceSession(deviceId);
    }

    @Override
    public DeviceSessionDTO stopSession(String deviceId) {
        Optional<Device> dev = deviceRepository.findById(deviceId);
        if (!dev.isPresent()) {
            throw new RuntimeException("DeviceNotFound");
        }
        Session currentActiveSession = sessionService.getDeviceActiveSession(deviceId);
        if (currentActiveSession != null) {
            currentActiveSession.setActive(false);
            sessionService.save(currentActiveSession);

            Record rec = new Record();
            rec.setDevice(dev.get());
            rec.setEnd(Instant.now());
            if (currentActiveSession.getOrders() != null && !currentActiveSession.getOrders().isEmpty()) {
                rec.setOrders(currentActiveSession.getOrders());
            }

            rec.setStart(currentActiveSession.getStart());
            recordService.save(rec);
        }

        return this.toDeviceSession(deviceId);
    }

    @Override
    public DeviceSessionDTO deviceWithSession(String deviceId) {
        return this.toDeviceSession(deviceId);
    }
}
