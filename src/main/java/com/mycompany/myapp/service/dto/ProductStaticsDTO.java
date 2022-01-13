package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Product} entity.
 */
public class ProductStaticsDTO implements Serializable {

    private String id;

    private String name;
    private int useCount = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public Double getUsePrice() {
        return usePrice;
    }

    public void setUsePrice(Double usePrice) {
        this.usePrice = usePrice;
    }

    private Double usePrice = 0.0;
}
