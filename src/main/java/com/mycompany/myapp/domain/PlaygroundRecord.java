package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Record.
 */
@Document(collection = "PlaygroundRecord")
public class PlaygroundRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start")
    private Instant start;

    @Field("end")
    private Instant end;

    @Field("total_price")
    private Double totalPrice = 0.0;

    @Field("total_price_time")
    private Double totalPriceTime = 0.0;

    @Field("total_price_orders")
    private Double totalPriceOrders = 0.0;

    @Field("total_price_user")
    private Double totalPriceUser = 0.0;

    @Field("duration")
    private Duration duration;

    @Field("totalNetPriceCalculated")
    private Double totalNetPriceCalculated = 0.0;

    @Field("ordersData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Set<Product> ordersData = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    @Field("previousSessionsTotalPrice")
    private Double previousSessionsTotalPrice = 0.0;

    public Double getPreviousSessionsTotalPrice() {
        return previousSessionsTotalPrice;
    }

    public void setPreviousSessionsTotalPrice(Double previousSessionsTotalPrice) {
        this.previousSessionsTotalPrice = previousSessionsTotalPrice;
    }

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
    //@JsonIgnore
    private List<PlaygroundRecord> previousSessions = new ArrayList<>();

    public List<PlaygroundRecord> getPreviousSessions() {
        return previousSessions;
    }

    public void setPreviousSessions(List<PlaygroundRecord> previousSessions) {
        this.previousSessions = previousSessions;
    }

    private int minutes = 0;
    private int hours = 0;

    public int getMinutes() {
        if (this.duration == null) {
            return 0;
        }
        this.minutes = this.duration.toMinutesPart();
        return this.duration.toMinutesPart();
    }

    public int getHours() {
        if (this.duration == null) {
            return 0;
        }
        this.hours = this.duration.toHoursPart();
        return this.duration.toHoursPart();
    }

    public Double getTotalNetPriceCalculated() {
        return totalNetPriceCalculated;
    }

    public void setTotalNetPriceCalculated(Double totalNetPriceCalculated) {
        this.totalNetPriceCalculated = totalNetPriceCalculated;
    }

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

    public PlaygroundRecord id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStart() {
        return this.start;
    }

    public PlaygroundRecord start(Instant start) {
        this.setStart(start);
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public PlaygroundRecord end(Instant end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public PlaygroundRecord totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPriceUser() {
        return totalPriceUser;
    }

    public void setTotalPriceUser(Double totalPriceUser) {
        this.totalPriceUser = totalPriceUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaygroundRecord)) {
            return false;
        }
        return id != null && id.equals(((PlaygroundRecord) o).id);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public Double getTotalPriceTime() {
        return totalPriceTime;
    }

    public void setTotalPriceTime(Double totalPriceTime) {
        this.totalPriceTime = totalPriceTime;
    }

    public Double getTotalPriceOrders() {
        return totalPriceOrders;
    }

    public void setTotalPriceOrders(Double totalPriceOrders) {
        this.totalPriceOrders = totalPriceOrders;
    }

    public Set<Product> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(Set<Product> ordersData) {
        this.ordersData = ordersData;
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
