package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ShopsOrders.
 */
@Document(collection = "shops_orders")
public class ShopsOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("total_price")
    private Double totalPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ShopsOrders id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ShopsOrders name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public ShopsOrders totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopsOrders)) {
            return false;
        }
        return id != null && id.equals(((ShopsOrders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopsOrders{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
