package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.OrderHandlerDomain;
import com.mycompany.myapp.domain.PosOrderRecord;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.OrderHandlerRepository;
import com.mycompany.myapp.repository.PosOrderRecoredRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ReceiptPosPrint;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PosService {

    private final OrderHandlerRepository orderHandlerRepository;
    private final PosOrderRecoredRepository recordRepository;

    public PosService(OrderHandlerRepository orderHandlerRepository, PosOrderRecoredRepository recordRepository) {
        this.orderHandlerRepository = orderHandlerRepository;
        this.recordRepository = recordRepository;
    }

    public void createNewPosOrderAction(
        String OrderOwnerName,
        OrderHandlerDomain.TABLE_TYPE type,
        Set<Product> productsList,
        HashMap<String, Integer> productsQuantatiy,
        boolean print
    ) {
        OrderHandlerDomain toCalculateDomain = new OrderHandlerDomain();
        toCalculateDomain.setOrdersData(productsList);
        toCalculateDomain.setOrdersQuantity(productsQuantatiy);
        toCalculateDomain.setDiscount(0.0);
        toCalculateDomain.setActive(false);
        toCalculateDomain.setType(type);
        toCalculateDomain.setName(OrderOwnerName);

        OrderHandlerDomain tableob = toCalculateDomain;
        Double netTotalPrice = tableob.getTotalPrice();
        tableob.setDiscount(0.0);
        tableob.setActive(false);

        OrderHandlerDomain savedTable = orderHandlerRepository.save(tableob);
        OrderHandlerDomain resTabel = calculateDeviceSessionOrderesPrice(savedTable);

        PosOrderRecord tableRecord = new PosOrderRecord();
        tableRecord.setDiscount(resTabel.getDiscount());

        Double calcPriceNet = calculateNetDeviceSessionOrderesPrice(resTabel);

        tableRecord.setTotalPrice(calcPriceNet);
        tableRecord.setOrdersData(resTabel.getOrdersData());
        tableRecord.setOrdersQuantity(resTabel.getOrdersQuantity());
        tableRecord.setTable(resTabel);
        tableRecord.setNetTotalPrice(calcPriceNet);
        tableRecord.setType(resTabel.getType());
        if (resTabel.getDiscount() != null && resTabel.getDiscount() > 0) {
            tableRecord.setTotalDiscountPrice((tableRecord.getNetTotalPrice() * resTabel.getDiscount()) / 100);
        } else {
            tableRecord.setTotalDiscountPrice(0.0);
        }

        PosOrderRecord SavedTableRecord = recordRepository.save(tableRecord);

        OrderHandlerDomain savedTableX = resetTable(resTabel);

        orderHandlerRepository.delete(savedTableX);

        if (print) {
            this.printPosRecord(SavedTableRecord);
        }
    }

    public void printPosRecord(PosOrderRecord SavedTableRecord) {
        Printable printable = new ReceiptPosPrint(SavedTableRecord);

        PrinterSupport ps = new PrinterSupport();

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(printable, ps.getPageFormat(pj));
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    OrderHandlerDomain resetTable(OrderHandlerDomain table) {
        table.setActive(false);
        table.setTotalPrice(0.0);
        table.setDiscount(0.0);
        table.setOrdersData(new HashSet<>());
        table.setOrdersQuantity(new HashMap<>());
        return orderHandlerRepository.save(table);
    }

    private Double calculateNetDeviceSessionOrderesPrice(OrderHandlerDomain table) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : table.getOrdersData()) {
            int prodValue;
            if (table.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = table.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 1;
            }
            Double prodPrice = order.getPrice();

            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }
        return totalCalculationsOfOrders;
    }

    private OrderHandlerDomain calculateDeviceSessionOrderesPrice(OrderHandlerDomain table) {
        Double totalCalculationsOfOrders = 0.0;
        for (Product order : table.getOrdersData()) {
            int prodValue;
            if (table.getOrdersQuantity().containsKey(order.getId())) {
                prodValue = table.getOrdersQuantity().get(order.getId());
            } else {
                prodValue = 1;
            }
            Double prodPrice = order.getPrice();

            Double totalProdPrice = Double.valueOf(prodValue) * prodPrice;
            totalCalculationsOfOrders += totalProdPrice;
        }

        if (table.getDiscount() != null && table.getDiscount() > 0 && table.getDiscount() < 100) {
            totalCalculationsOfOrders = (double) Math.round((100 - table.getDiscount()) * totalCalculationsOfOrders / 100);
        }
        table.setTotalPrice(totalCalculationsOfOrders);
        return orderHandlerRepository.save(table);
    }
}
