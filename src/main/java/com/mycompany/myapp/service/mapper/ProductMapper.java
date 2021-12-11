package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.service.dto.ProductDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    ProductDTO toDto(Product s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ProductDTO> toDtoIdSet(Set<Product> product);
}
