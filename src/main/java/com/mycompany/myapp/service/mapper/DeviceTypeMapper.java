package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.DeviceType;
import com.mycompany.myapp.service.dto.DeviceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceType} and its DTO {@link DeviceTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceTypeMapper extends EntityMapper<DeviceTypeDTO, DeviceType> {
    @Named("id")
    DeviceTypeDTO toDtoId(DeviceType deviceType);
}
