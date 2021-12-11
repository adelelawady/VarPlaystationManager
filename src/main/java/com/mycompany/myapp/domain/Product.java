package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Product.
 */
@Document(collection = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("price")
    private Double price;

    @DBRef
    @Field("category")
    private Category category;

    @DBRef
    @Field("sessions")
    @JsonIgnoreProperties(value = { "device", "orders" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @DBRef
    @Field("records")
    @JsonIgnoreProperties(value = { "device", "orders" }, allowSetters = true)
    private Set<Record> records = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Product id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return this.price;
    }

    public Product price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions) {
        if (this.sessions != null) {
            this.sessions.forEach(i -> i.removeOrders(this));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.addOrders(this));
        }
        this.sessions = sessions;
    }

    public Product sessions(Set<Session> sessions) {
        this.setSessions(sessions);
        return this;
    }

    public Product addSession(Session session) {
        this.sessions.add(session);
        session.getOrders().add(this);
        return this;
    }

    public Product removeSession(Session session) {
        this.sessions.remove(session);
        session.getOrders().remove(this);
        return this;
    }

    public Set<Record> getRecords() {
        return this.records;
    }

    public void setRecords(Set<Record> records) {
        if (this.records != null) {
            this.records.forEach(i -> i.removeOrders(this));
        }
        if (records != null) {
            records.forEach(i -> i.addOrders(this));
        }
        this.records = records;
    }

    public Product records(Set<Record> records) {
        this.setRecords(records);
        return this;
    }

    public Product addRecord(Record record) {
        this.records.add(record);
        record.getOrders().add(this);
        return this;
    }

    public Product removeRecord(Record record) {
        this.records.remove(record);
        record.getOrders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
