package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Device;
import com.mycompany.myapp.domain.Playground;
import com.mycompany.myapp.service.dto.DeviceDTO;
import com.mycompany.myapp.service.dto.DeviceSessionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeviceTypeMapper.class })
public interface PlaygroundMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "type", source = "type", qualifiedByName = "id")
    DeviceDTO toDto(Device s);

    DeviceSessionDTO toDeviceSessionDTO(Playground d);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeviceDTO toDtoId(Device device);
}
