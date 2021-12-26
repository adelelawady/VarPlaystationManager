package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Table.
 */
@Document(collection = "table")
public class Table implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("discount")
    private Double discount;

    @Field("total_price")
    private Double totalPrice;

    @Field("active")
    private boolean active = false;

    @Field("ordersData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Set<Product> ordersData = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return this.id;
    }

    public Table id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Table name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Table discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Table totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Product> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(Set<Product> ordersData) {
        this.ordersData = ordersData;
    }

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(HashMap<String, Integer> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Table)) {
            return false;
        }
        return id != null && id.equals(((Table) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Table{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discount=" + getDiscount() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
