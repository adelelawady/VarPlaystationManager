package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Session;
import com.mycompany.myapp.service.dto.SessionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Session} and its DTO {@link SessionDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeviceMapper.class, ProductMapper.class })
public interface SessionMapper extends EntityMapper<SessionDTO, Session> {
    @Mapping(target = "device", source = "device", qualifiedByName = "id")
    @Mapping(target = "orders", source = "orders", qualifiedByName = "idSet")
    SessionDTO toDto(Session s);

    @Mapping(target = "removeOrders", ignore = true)
    Session toEntity(SessionDTO sessionDTO);
}
