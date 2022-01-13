package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Device.
 */
@Document(collection = "sheft")
public class Sheft extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("user")
    private User user;

    @Field("start")
    private Instant start = Instant.now();

    @Field("end")
    private Instant end = null;

    @Field("total_net_price")
    private Double total_net_price = 0.0;

    @Field("total_discount_price")
    private Double total_discount = 0.0;

    @Field("total_net_price_after_discount")
    private Double total_net_price_after_discount = 0.0;

    @Field("total_net_price_after_discount_systen")
    private Double total_net_price_after_discount_system = 0.0;

    @Field("total_net_price_devices")
    private Double total_net_price_devices = 0.0;

    @Field("total_net_user_price_devices")
    private Double total_net_user_price_devices = 0.0;

    @Field("total_discount_price_devices")
    private Double total_discount_price_devices = 0.0;

    @Field("total_price_time_devices")
    private Double total_price_time_devices = 0.0;

    @Field("total_price_orders_devices")
    private Double total_price_orders_devices = 0.0;

    @Field("total_net_price_Tables")
    private Double total_net_price_Tables = 0.0;

    @Field("total_discount_price_Tables")
    private Double total_discount_price_Tables = 0.0;

    @Field("total_net_price_after_discount_Tables")
    private Double total_net_price_after_discount_Tables = 0.0;

    @Field("total_net_price_takeaway")
    private Double total_net_price_takeaway = 0.0;

    @Field("total_discount_price_takeaway")
    private Double total_discount_price_takeaway = 0.0;

    @Field("total_net_price_after_discount_takeaway")
    private Double total_net_price_after_discount_takeaway = 0.0;

    @Field("total_net_price_shops")
    private Double total_net_price_shops = 0.0;

    @Field("total_discount_price_shops")
    private Double total_discount_price_shops = 0.0;

    @Field("total_net_price_after_discount_shops")
    private Double total_net_price_after_discount_shops = 0.0;

    @DBRef
    List<Record> records = new ArrayList<>();

    @DBRef
    List<TableRecord> tableRecords = new ArrayList<>();

    @DBRef
    List<TableRecord> tableTakeAwayRecords = new ArrayList<>();

    public List<TableRecord> getTableTakeAwayRecords() {
        return tableTakeAwayRecords;
    }

    public void setTableTakeAwayRecords(List<TableRecord> tableTakeAwayRecords) {
        this.tableTakeAwayRecords = tableTakeAwayRecords;
    }

    public List<TableRecord> getTableShopsRecords() {
        return tableShopsRecords;
    }

    public void setTableShopsRecords(List<TableRecord> tableShopsRecords) {
        this.tableShopsRecords = tableShopsRecords;
    }

    @DBRef
    List<TableRecord> tableShopsRecords = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Double getTotal_net_price() {
        return total_net_price;
    }

    public void setTotal_net_price(Double total_net_price) {
        this.total_net_price = total_net_price;
    }

    public Double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(Double total_discount) {
        this.total_discount = total_discount;
    }

    public Double getTotal_net_price_after_discount() {
        return total_net_price_after_discount;
    }

    public void setTotal_net_price_after_discount(Double total_net_price_after_discount) {
        this.total_net_price_after_discount = total_net_price_after_discount;
    }

    public Double getTotal_net_price_devices() {
        return total_net_price_devices;
    }

    public void setTotal_net_price_devices(Double total_net_price_devices) {
        this.total_net_price_devices = total_net_price_devices;
    }

    public Double getTotal_net_user_price_devices() {
        return total_net_user_price_devices;
    }

    public void setTotal_net_user_price_devices(Double total_net_user_price_devices) {
        this.total_net_user_price_devices = total_net_user_price_devices;
    }

    public Double getTotal_discount_price_devices() {
        return total_discount_price_devices;
    }

    public void setTotal_discount_price_devices(Double total_discount_price_devices) {
        this.total_discount_price_devices = total_discount_price_devices;
    }

    public Double getTotal_price_time_devices() {
        return total_price_time_devices;
    }

    public void setTotal_price_time_devices(Double total_discount_price_time_devices) {
        this.total_price_time_devices = total_discount_price_time_devices;
    }

    public Double getTotal_price_orders_devices() {
        return total_price_orders_devices;
    }

    public void setTotal_price_orders_devices(Double total_discount_price_orders_devices) {
        this.total_price_orders_devices = total_discount_price_orders_devices;
    }

    public Double getTotal_net_price_Tables() {
        return total_net_price_Tables;
    }

    public void setTotal_net_price_Tables(Double total_net_price_Tables) {
        this.total_net_price_Tables = total_net_price_Tables;
    }

    public Double getTotal_discount_price_Tables() {
        return total_discount_price_Tables;
    }

    public void setTotal_discount_price_Tables(Double total_discount_price_Tables) {
        this.total_discount_price_Tables = total_discount_price_Tables;
    }

    public Double getTotal_net_price_after_discount_Tables() {
        return total_net_price_after_discount_Tables;
    }

    public void setTotal_net_price_after_discount_Tables(Double total_net_price_after_discount_Tables) {
        this.total_net_price_after_discount_Tables = total_net_price_after_discount_Tables;
    }

    public Double getTotal_net_price_takeaway() {
        return total_net_price_takeaway;
    }

    public void setTotal_net_price_takeaway(Double total_net_price_takeaway) {
        this.total_net_price_takeaway = total_net_price_takeaway;
    }

    public Double getTotal_discount_price_takeaway() {
        return total_discount_price_takeaway;
    }

    public void setTotal_discount_price_takeaway(Double total_discount_price_takeaway) {
        this.total_discount_price_takeaway = total_discount_price_takeaway;
    }

    public Double getTotal_net_price_after_discount_takeaway() {
        return total_net_price_after_discount_takeaway;
    }

    public void setTotal_net_price_after_discount_takeaway(Double total_net_price_after_discount_takeaway) {
        this.total_net_price_after_discount_takeaway = total_net_price_after_discount_takeaway;
    }

    public Double getTotal_net_price_shops() {
        return total_net_price_shops;
    }

    public void setTotal_net_price_shops(Double total_net_price_shops) {
        this.total_net_price_shops = total_net_price_shops;
    }

    public Double getTotal_discount_price_shops() {
        return total_discount_price_shops;
    }

    public void setTotal_discount_price_shops(Double total_discount_price_shops) {
        this.total_discount_price_shops = total_discount_price_shops;
    }

    public Double getTotal_net_price_after_discount_shops() {
        return total_net_price_after_discount_shops;
    }

    public void setTotal_net_price_after_discount_shops(Double total_net_price_after_discount_shops) {
        this.total_net_price_after_discount_shops = total_net_price_after_discount_shops;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<TableRecord> getTableRecords() {
        return tableRecords;
    }

    public void setTableRecords(List<TableRecord> tableRecords) {
        this.tableRecords = tableRecords;
    }

    public Double getTotal_net_price_after_discount_system() {
        return total_net_price_after_discount_system;
    }

    public void setTotal_net_price_after_discount_system(Double total_net_price_after_discount_system) {
        this.total_net_price_after_discount_system = total_net_price_after_discount_system;
    }
}
