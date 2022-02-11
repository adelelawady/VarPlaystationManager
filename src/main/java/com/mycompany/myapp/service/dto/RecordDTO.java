package com.mycompany.myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Record} entity.
 */
public class RecordDTO implements Serializable {

    private String id;

    private Instant start;

    private Instant end;

    private Double totalPrice;

    private DeviceDTO device;

    private Double totalPriceUser;

    private Double ordersDiscount = 0.0;

    private Double timeDiscount = 0.0;

    private Duration duration;

    private Double totalPriceTime = 0.0;

    private Double previousSessionsTotalPrice = 0.0;

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

    public Double getOrdersDiscount() {
        return ordersDiscount;
    }

    public void setOrdersDiscount(Double ordersDiscount) {
        this.ordersDiscount = ordersDiscount;
    }

    public Double getTimeDiscount() {
        return timeDiscount;
    }

    public void setTimeDiscount(Double timeDiscount) {
        this.timeDiscount = timeDiscount;
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

    private Double totalPriceOrders;

    private int minutes;

    public int getMinutes() {
        return this.duration.toMinutesPart();
    }

    public int getHours() {
        return this.duration.toHoursPart();
    }

    private int hours;

    private Set<Product> ordersData = new HashSet<>();

    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(HashMap<String, Integer> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecordDTO)) {
            return false;
        }

        RecordDTO recordDTO = (RecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public Double getTotalPriceUser() {
        return totalPriceUser;
    }

    public void setTotalPriceUser(Double totalPriceUser) {
        this.totalPriceUser = totalPriceUser;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        return "RecordDTO{" +
            "id='" + getId() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", device=" + getDevice() +
            "}";
    }

    public Double getPreviousSessionsTotalPrice() {
        return previousSessionsTotalPrice;
    }

    public void setPreviousSessionsTotalPrice(Double previousSessionsTotalPrice) {
        this.previousSessionsTotalPrice = previousSessionsTotalPrice;
    }

    public List<Record> getPreviousSessions() {
        return previousSessions;
    }

    public void setPreviousSessions(List<Record> previousSessions) {
        this.previousSessions = previousSessions;
    }
}
