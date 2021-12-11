package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DeviceType.
 */
@Document(collection = "device_type")
public class DeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("price_per_hour")
    private Double pricePerHour;

    @Field("price_per_hour_multi")
    private Double pricePerHourMulti;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Double getPricePerHourMulti() {
        return pricePerHourMulti;
    }

    public void setPricePerHourMulti(Double pricePerHourMulti) {
        this.pricePerHourMulti = pricePerHourMulti;
    }

    public String getId() {
        return this.id;
    }

    public DeviceType id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DeviceType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPricePerHour() {
        return this.pricePerHour;
    }

    public DeviceType pricePerHour(Double pricePerHour) {
        this.setPricePerHour(pricePerHour);
        return this;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceType)) {
            return false;
        }
        return id != null && id.equals(((DeviceType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pricePerHour=" + getPricePerHour() +
            "}";
    }
}
