/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adelelawady.konsol_license;

import java.io.Serializable;

/**
 *
 * @author mymac
 */
public class LicenseCheckModel implements Serializable {

    private static final long serialVersionUID = 8600137500316662317L;

    private String ProductId = "";

    private String vendor = "";

    private String processorSerialNumber = "";
    private String processorIdentifier = "";
    private String processors = "";
    private String OwnerName = "";

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProcessorSerialNumber() {
        return processorSerialNumber;
    }

    public void setProcessorSerialNumber(String processorSerialNumber) {
        this.processorSerialNumber = processorSerialNumber;
    }

    public String getProcessorIdentifier() {
        return processorIdentifier;
    }

    public void setProcessorIdentifier(String processorIdentifier) {
        this.processorIdentifier = processorIdentifier;
    }

    public String getProcessors() {
        return processors;
    }

    public void setProcessors(String processors) {
        this.processors = processors;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }

    @Override
    public String toString() {
        return (
            "LicenseModel{" +
            "ProductId=" +
            ProductId +
            ", vendor=" +
            vendor +
            ", processorSerialNumber=" +
            processorSerialNumber +
            ", processorIdentifier=" +
            processorIdentifier +
            ", processors=" +
            processors +
            ", OwnerName=" +
            OwnerName +
            '}'
        );
    }
}
