package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Record.
 */
@Document(collection = "record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start")
    private Instant start;

    @Field("end")
    private Instant end;

    @Field("total_price")
    private Double totalPrice;

    @DBRef
    @Field("device")
    private Device device;

    @DBRef
    @Field("orders")
    @JsonIgnoreProperties(value = { "category", "sessions", "records" }, allowSetters = true)
    private Set<Product> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Record id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStart() {
        return this.start;
    }

    public Record start(Instant start) {
        this.setStart(start);
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public Record end(Instant end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Record totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Record device(Device device) {
        this.setDevice(device);
        return this;
    }

    public Set<Product> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Product> products) {
        this.orders = products;
    }

    public Record orders(Set<Product> products) {
        this.setOrders(products);
        return this;
    }

    public Record addOrders(Product product) {
        this.orders.add(product);
        product.getRecords().add(this);
        return this;
    }

    public Record removeOrders(Product product) {
        this.orders.remove(product);
        product.getRecords().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        return id != null && id.equals(((Record) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
