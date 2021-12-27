package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private String id;

    private String name;

    private Double price;

    private Double takeawayPrice;

    public Double getTakeawayPrice() {
        return takeawayPrice;
    }

    public void setTakeawayPrice(Double takeawayPrice) {
        this.takeawayPrice = takeawayPrice;
    }

    private Double shopsPrice;

    public Double getShopsPrice() {
        return shopsPrice;
    }

    public void setShopsPrice(Double shopsPrice) {
        this.shopsPrice = shopsPrice;
    }

    private CategoryDTO category;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", category=" + getCategory() +
            "}";
    }
}
