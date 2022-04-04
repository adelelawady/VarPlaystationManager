package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Volume;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.MetricPrefix;

/**
 * A Product.
 */
@Document(collection = "product")
public class Product extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("price")
    private Double price = 0.0;

    @Field("takeawayPrice")
    private Double takeawayPrice = 0.0;

    @Field("shopsPrice")
    private Double shopsPrice = 0.0;

    @Field("enName")
    private String enName;

    @Field("subProducts")
    private Set<Product> subProducts = new HashSet<>();

    @Field("hasParent")
    private Boolean hasParent = false;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Field("parent")
    private String parent;

    public Set<Product> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(Set<Product> subProducts) {
        this.subProducts = subProducts;
    }

    public Boolean getHasParent() {
        return hasParent;
    }

    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }

    @Field("ordered")
    private boolean ordered = false;

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public String getEnName() {
        if (enName == null || enName.isBlank()) {
            return name;
        }
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Double getTakeawayPrice() {
        return takeawayPrice;
    }

    public void setTakeawayPrice(Double takeawayPrice) {
        this.takeawayPrice = takeawayPrice;
    }

    public Double getShopsPrice() {
        return shopsPrice;
    }

    public void setShopsPrice(Double shopsPrice) {
        this.shopsPrice = shopsPrice;
    }

    @DBRef
    @Field("category")
    private Category category;

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
	@Override
	public String toString() {
		return "Product{" + "id=" + getId() + ", name='" + getName() + "'" + ", price=" + getPrice() + "}";
	}
}
