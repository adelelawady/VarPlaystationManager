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
public class SessionEndDTO implements Serializable {

    private Double totalPrice = 0.0;

    private Double ordersDiscount = 0.0;

    private Double timeDiscount = 0.0;

    private boolean print = true;

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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }
}
