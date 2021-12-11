package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.DeviceType;
import com.mycompany.myapp.repository.DeviceTypeRepository;
import com.mycompany.myapp.service.DeviceTypeService;
import com.mycompany.myapp.service.dto.DeviceTypeDTO;
import com.mycompany.myapp.service.mapper.DeviceTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DeviceType}.
 */
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final Logger log = LoggerFactory.getLogger(DeviceTypeServiceImpl.class);

    private final DeviceTypeRepository deviceTypeRepository;

    private final DeviceTypeMapper deviceTypeMapper;

    public DeviceTypeServiceImpl(DeviceTypeRepository deviceTypeRepository, DeviceTypeMapper deviceTypeMapper) {
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceTypeMapper = deviceTypeMapper;
    }

    @Override
    public DeviceTypeDTO save(DeviceTypeDTO deviceTypeDTO) {
        log.debug("Request to save DeviceType : {}", deviceTypeDTO);
        DeviceType deviceType = deviceTypeMapper.toEntity(deviceTypeDTO);
        deviceType = deviceTypeRepository.save(deviceType);
        return deviceTypeMapper.toDto(deviceType);
    }

    @Override
    public Optional<DeviceTypeDTO> partialUpdate(DeviceTypeDTO deviceTypeDTO) {
        log.debug("Request to partially update DeviceType : {}", deviceTypeDTO);

        return deviceTypeRepository
            .findById(deviceTypeDTO.getId())
            .map(existingDeviceType -> {
                deviceTypeMapper.partialUpdate(existingDeviceType, deviceTypeDTO);

                return existingDeviceType;
            })
            .map(deviceTypeRepository::save)
            .map(deviceTypeMapper::toDto);
    }

    @Override
    public Page<DeviceTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceTypes");
        return deviceTypeRepository.findAll(pageable).map(deviceTypeMapper::toDto);
    }

    @Override
    public Optional<DeviceTypeDTO> findOne(String id) {
        log.debug("Request to get DeviceType : {}", id);
        return deviceTypeRepository.findById(id).map(deviceTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DeviceType : {}", id);
        deviceTypeRepository.deleteById(id);
    }
}
