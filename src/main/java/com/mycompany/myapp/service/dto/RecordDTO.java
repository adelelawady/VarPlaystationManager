package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Record} entity.
 */
public class RecordDTO implements Serializable {

    private String id;

    private Instant start;

    private Instant end;

    private Double totalPrice;

    private DeviceDTO device;

    private Set<ProductDTO> orders = new HashSet<>();

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

    public Set<ProductDTO> getOrders() {
        return orders;
    }

    public void setOrders(Set<ProductDTO> orders) {
        this.orders = orders;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordDTO{" +
            "id='" + getId() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", device=" + getDevice() +
            ", orders=" + getOrders() +
            "}";
    }
}
