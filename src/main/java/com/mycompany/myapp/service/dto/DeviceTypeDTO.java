package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.DeviceType} entity.
 */
public class DeviceTypeDTO implements Serializable {

    private String id;

    private String name;

    private Double pricePerHour = 0.0;

    private Double pricePerHourMulti = 0.0;

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

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceTypeDTO)) {
            return false;
        }

        DeviceTypeDTO deviceTypeDTO = (DeviceTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public Double getPricePerHourMulti() {
        return pricePerHourMulti;
    }

    public void setPricePerHourMulti(Double pricePerHourMulti) {
        this.pricePerHourMulti = pricePerHourMulti;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceTypeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", pricePerHour=" + getPricePerHour() +
            "}";
    }
}
