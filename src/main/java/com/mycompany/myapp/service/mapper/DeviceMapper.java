package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.service.dto.DeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeviceTypeMapper.class })
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "type", source = "type", qualifiedByName = "id")
    DeviceDTO toDto(Device s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeviceDTO toDtoId(Device device);
}
