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

    @Field("total_net_price_time_devices")
    private Double total_net_price_devices = 0.0;

    @Field("total_net_price_Tables")
    private Double total_net_price_Tables = 0.0;

    public Double getTotal_net_price_Tables() {
        return total_net_price_Tables;
    }

    public void setTotal_net_price_Tables(Double total_net_price_Tables) {
        this.total_net_price_Tables = total_net_price_Tables;
    }

    public Double getTotal_net_price_playground() {
        return total_net_price_playground;
    }

    public void setTotal_net_price_playground(Double total_net_price_playground) {
        this.total_net_price_playground = total_net_price_playground;
    }

    public Double getTotal_net_price_market() {
        return total_net_price_market;
    }

    public void setTotal_net_price_market(Double total_net_price_market) {
        this.total_net_price_market = total_net_price_market;
    }

    public Double getTotal_net_price_res() {
        return total_net_price_res;
    }

    public void setTotal_net_price_res(Double total_net_price_res) {
        this.total_net_price_res = total_net_price_res;
    }

    public Double getTotal_net_price_cafe() {
        return total_net_price_cafe;
    }

    public void setTotal_net_price_cafe(Double total_net_price_cafe) {
        this.total_net_price_cafe = total_net_price_cafe;
    }

    @Field("total_net_price_playground")
    private Double total_net_price_playground = 0.0;

    @Field("total_net_price_market")
    private Double total_net_price_market = 0.0;

    @Field("total_net_price_res")
    private Double total_net_price_res = 0.0;

    @Field("total_net_price_cafe")
    private Double total_net_price_cafe = 0.0;

    @DBRef
    List<Record> records = new ArrayList<>();

    @DBRef
    List<TableRecord> tableRecords = new ArrayList<>();

    public List<PlaygroundRecord> getPlaygroundRecords() {
        return playgroundRecords;
    }

    public void setPlaygroundRecords(List<PlaygroundRecord> playgroundRecords) {
        this.playgroundRecords = playgroundRecords;
    }

    public List<PosOrderRecord> getMarketRecords() {
        return marketRecords;
    }

    public void setMarketRecords(List<PosOrderRecord> marketRecords) {
        this.marketRecords = marketRecords;
    }

    public List<PosOrderRecord> getResRecords() {
        return resRecords;
    }

    public void setResRecords(List<PosOrderRecord> resRecords) {
        this.resRecords = resRecords;
    }

    public List<PosOrderRecord> getCafeRecords() {
        return cafeRecords;
    }

    public void setCafeRecords(List<PosOrderRecord> cafeRecords) {
        this.cafeRecords = cafeRecords;
    }

    @DBRef
    List<PlaygroundRecord> playgroundRecords = new ArrayList<>();

    @DBRef
    List<PosOrderRecord> marketRecords = new ArrayList<>();

    @DBRef
    List<PosOrderRecord> resRecords = new ArrayList<>();

    @DBRef
    List<PosOrderRecord> cafeRecords = new ArrayList<>();

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

    public Double getTotal_net_price_devices() {
        return total_net_price_devices;
    }

    public void setTotal_net_price_devices(Double total_net_price_devices) {
        this.total_net_price_devices = total_net_price_devices;
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
}
