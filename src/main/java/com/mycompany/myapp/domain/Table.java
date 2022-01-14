package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class Table extends AbstractAuditingEntity implements Serializable {

    public enum TABLE_TYPE {
        TABLE("table"),
        TAKEAWAY("takeaway"),
        SHOPS("shops");

        private final String value;

        TABLE_TYPE(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value.toLowerCase();
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TABLE_TYPE fromValue(String value) {
            for (TABLE_TYPE b : TABLE_TYPE.values()) {
                if (b.getValue().equalsIgnoreCase(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected TABLE_TYPE value '" + value + "'");
        }
    }

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("discount")
    private Double discount = 0.0;

    @Field("total_price")
    private Double totalPrice = 0.0;

    @Field("active")
    private boolean active = false;

    @Field("ordersData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Set<Product> ordersData = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    @Field("type")
    private TABLE_TYPE type = TABLE_TYPE.TABLE;

    @Field("index")
    private Integer index = 0;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    public Set<Product> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(Set<Product> ordersData) {
        this.ordersData = ordersData;
    }

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public TABLE_TYPE getType() {
        return type;
    }

    public void setType(TABLE_TYPE type) {
        this.type = type;
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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
	@Override
	public String toString() {
		return "Table{" + "id=" + getId() + ", name='" + getName() + "'" + ", discount=" + getDiscount()
				+ ", totalPrice=" + getTotalPrice() + "}";
	}
}
