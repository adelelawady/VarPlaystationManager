package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Device} entity.
 */
public class AdminReportDTO implements Serializable {

    private Double TotalPrice = 0.0;

    private Double TotalPriceTimeDevices = 0.0;

    private Double TotalPriceDevices = 0.0;

    private Double totalPriceOrdersDevices = 0.0;

    private Double totalPriceUserDevices = 0.0;

    public Double getTotalPriceUserDevices() {
        return totalPriceUserDevices;
    }

    public void setTotalPriceUserDevices(Double totalPriceUserDevices) {
        this.totalPriceUserDevices = totalPriceUserDevices;
    }

    private Double totalPriceOrdersTables = 0.0;

    private Double totalPriceOrdersTakeAway = 0.0;

    private Double totalPriceOrdersShops = 0.0;

    public Double getTotalPriceDevices() {
        return TotalPriceDevices;
    }

    public void setTotalPriceDevices(Double totalPriceDevices) {
        TotalPriceDevices = totalPriceDevices;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }

    public Double getTotalPriceTimeDevices() {
        return TotalPriceTimeDevices;
    }

    public void setTotalPriceTimeDevices(Double totalPriceTimeDevices) {
        TotalPriceTimeDevices = totalPriceTimeDevices;
    }

    public Double getTotalPriceOrdersDevices() {
        return totalPriceOrdersDevices;
    }

    public void setTotalPriceOrdersDevices(Double totalPriceOrdersDevices) {
        this.totalPriceOrdersDevices = totalPriceOrdersDevices;
    }

    public Double getTotalPriceOrdersTables() {
        return totalPriceOrdersTables;
    }

    public void setTotalPriceOrdersTables(Double totalPriceOrdersTables) {
        this.totalPriceOrdersTables = totalPriceOrdersTables;
    }

    public Double getTotalPriceOrdersTakeAway() {
        return totalPriceOrdersTakeAway;
    }

    public void setTotalPriceOrdersTakeAway(Double totalPriceOrdersTakeAway) {
        this.totalPriceOrdersTakeAway = totalPriceOrdersTakeAway;
    }

    public Double getTotalPriceOrdersShops() {
        return totalPriceOrdersShops;
    }

    public void setTotalPriceOrdersShops(Double totalPriceOrdersShops) {
        this.totalPriceOrdersShops = totalPriceOrdersShops;
    }
}
