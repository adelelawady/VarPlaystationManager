package com.mycompany.myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.Product;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Record} entity.
 */
public class RecordsFilterDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Double totalPrice = 0.0;

    private Double totalPriceUser = 0.0;

    private Double totalPriceTime = 0.0;

    private Double totalPriceOrders = 0.0;

    private int totalMinutes = 0;

    private int totalHours = 0;

    private Double ordersDiscount = 0.0;

    private Double timeDiscount = 0.0;

    private Double previousSessionsTotalPrice = 0.0;

    public Double getOrdersDiscount() {
        return ordersDiscount;
    }

    public void setOrdersDiscount(Double ordersDiscount) {
        this.ordersDiscount = ordersDiscount;
    }

    public Double getTimeDiscount() {
        return timeDiscount;
    }

    public void setTimeDiscount(Double timeDiscount) {
        this.timeDiscount = timeDiscount;
    }

    private Page<RecordDTO> resultList;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPriceUser() {
        return totalPriceUser;
    }

    public void setTotalPriceUser(Double totalPriceUser) {
        this.totalPriceUser = totalPriceUser;
    }

    public Double getTotalPriceTime() {
        return totalPriceTime;
    }

    public void setTotalPriceTime(Double totalPriceTime) {
        this.totalPriceTime = totalPriceTime;
    }

    public Double getTotalPriceOrders() {
        return totalPriceOrders;
    }

    public void setTotalPriceOrders(Double totalPriceOrders) {
        this.totalPriceOrders = totalPriceOrders;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public Page<RecordDTO> getResultList() {
        return resultList;
    }

    public void setResultList(Page<RecordDTO> resultList) {
        this.resultList = resultList;
    }

    public Double getPreviousSessionsTotalPrice() {
        return previousSessionsTotalPrice;
    }

    public void setPreviousSessionsTotalPrice(Double previousSessionsTotalPrice) {
        this.previousSessionsTotalPrice = previousSessionsTotalPrice;
    }
}
