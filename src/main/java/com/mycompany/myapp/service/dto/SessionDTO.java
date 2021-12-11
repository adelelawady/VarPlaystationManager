package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Session} entity.
 */
public class SessionDTO implements Serializable {

    private String id;

    private Instant start;

    private Double reserved;

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

    public Double getReserved() {
        return reserved;
    }

    public void setReserved(Double reserved) {
        this.reserved = reserved;
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
        if (!(o instanceof SessionDTO)) {
            return false;
        }

        SessionDTO sessionDTO = (SessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionDTO{" +
            "id='" + getId() + "'" +
            ", start='" + getStart() + "'" +
            ", reserved=" + getReserved() +
            ", device=" + getDevice() +
            ", orders=" + getOrders() +
            "}";
    }
}
