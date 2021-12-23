package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Session.
 */
@Document(collection = "session")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start")
    private Instant start;

    @Field("reserved")
    private Double reserved;

    @Field("active")
    private boolean active;

    @Field("multi")
    private boolean multi = false;

    @Field("discount")
    private Double discount = 0.0;

    @Field("ordersPrice")
    private Double ordersPrice = 0.0;

    public Double getOrdersPrice() {
        return ordersPrice;
    }

    public void setOrdersPrice(Double ordersPrice) {
        this.ordersPrice = ordersPrice;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    @DBRef
    @Field("device")
    private Device device;

    @DBRef
    @Field("orders")
    @JsonIgnoreProperties(value = { "category", "sessions", "records" }, allowSetters = true)
    private Set<Product> orders = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(HashMap<String, Integer> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    public String getId() {
        return this.id;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Session id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStart() {
        return this.start;
    }

    public Session start(Instant start) {
        this.setStart(start);
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Double getReserved() {
        return this.reserved;
    }

    public Session reserved(Double reserved) {
        this.setReserved(reserved);
        return this;
    }

    public void setReserved(Double reserved) {
        this.reserved = reserved;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Session device(Device device) {
        this.setDevice(device);
        return this;
    }

    public Set<Product> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Product> products) {
        this.orders = products;
    }

    public Session orders(Set<Product> products) {
        this.setOrders(products);
        return this;
    }

    public Session addOrders(Product product) {
        this.orders.add(product);
        return this;
    }

    public Session removeOrders(Product product) {
        this.orders.remove(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return id != null && id.equals(((Session) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", reserved=" + getReserved() +
            "}";
    }
}
