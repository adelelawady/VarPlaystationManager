package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.service.dto.RecordDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Record} and its DTO {@link RecordDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeviceMapper.class, ProductMapper.class })
public interface RecordMapper extends EntityMapper<RecordDTO, Record> {
    @Mapping(target = "device", source = "device", qualifiedByName = "id")
    @Mapping(target = "orders", source = "orders", qualifiedByName = "idSet")
    RecordDTO toDto(Record s);

    @Mapping(target = "removeOrders", ignore = true)
    Record toEntity(RecordDTO recordDTO);
}
