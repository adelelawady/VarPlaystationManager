package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Record.
 */
@Document(collection = "PosRecord")
public class PosOrderRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("table")
    private OrderHandlerDomain table;

    @Field("total_price")
    private Double totalPrice = 0.0;

    @Field("total_discount_price")
    private Double totalDiscountPrice = 0.0;

    public Double getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public void setTotalDiscountPrice(Double totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    @Field("net_total_price")
    private Double netTotalPrice = 0.0;

    @Field("discount")
    private Double discount = 0.0;

    @Field("type")
    private OrderHandlerDomain.TABLE_TYPE type = OrderHandlerDomain.TABLE_TYPE.CAFE;

    public OrderHandlerDomain.TABLE_TYPE getType() {
        return type;
    }

    public void setType(OrderHandlerDomain.TABLE_TYPE type) {
        this.type = type;
    }

    @Field("ordersData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Set<Product> ordersData = new HashSet<>();

    @Field("ordersQuantity")
    private HashMap<String, Integer> ordersQuantity = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderHandlerDomain getTable() {
        return table;
    }

    public void setTable(OrderHandlerDomain table) {
        this.table = table;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Set<Product> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(Set<Product> ordersData) {
        this.ordersData = ordersData;
    }

    public HashMap<String, Integer> getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(HashMap<String, Integer> ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Double getNetTotalPrice() {
        return netTotalPrice;
    }

    public void setNetTotalPrice(Double netTotalPrice) {
        this.netTotalPrice = netTotalPrice;
    }
}
