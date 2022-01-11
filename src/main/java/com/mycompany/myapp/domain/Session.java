package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    @Field("deviceId")
    private String deviceId;

    @Field("deviceName")
    private String deviceName;

    @Field("reserved")
    private Double reserved;

    @Field("previousSessionsTotalPrice")
    private Double previousSessionsTotalPrice = 0.0;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    // @DBRef
    // @Field("device")
    // @JsonIgnoreProperties(value = {"session" }, allowSetters = true)
    // private Device device;

    @DBRef
    @Field("orders")
    @JsonIgnoreProperties(value = { "category", "sessions", "records" }, allowSetters = true)
    private Set<Product> orders = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    @Field("previousSessions")
    @JsonIgnoreProperties(
        value = {
            "device.session",
            "device.category",
            "device.type",
            "ordersData",
            "ordersQuantity",
            "previousSessions",
            "id",
            "totalPriceOrders",
            "totalPriceUser",
            "ordersDiscount",
            "timeDiscount",
            "duration",
            "createdDate",
            "totalPrice",
        },
        allowSetters = true
    )
    private List<Record> previousSessions = new ArrayList<>();

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

    //  public Device getDevice() {
    //      return this.device;
    //  }

    //  public void setDevice(Device device) {
    //      this.device = device;
    //  }

    //  public Session device(Device device) {
    //      this.setDevice(device);
    //      return this;
    //  }

    public Set<Product> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Product> products) {
        this.orders = products;
    }

    public Double getPreviousSessionsTotalPrice() {
        return previousSessionsTotalPrice;
    }

    public void setPreviousSessionsTotalPrice(Double previousSessionsTotalPrice) {
        this.previousSessionsTotalPrice = previousSessionsTotalPrice;
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

    public List<Record> getPreviousSessions() {
        return previousSessions;
    }

    public void setPreviousSessions(List<Record> previousSessions) {
        this.previousSessions = previousSessions;
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
