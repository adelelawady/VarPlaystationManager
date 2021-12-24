package com.mycompany.myapp.service;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;

public class TestPrinter {

    public boolean done = false;
    public PageFormat format = new PageFormat();
    public Paper paper = new Paper();
    public PrintRequestAttributeSet aset;

    public void initPrinter() {
        double paperWidth = 3; //3.25
        double paperHeight = 500.69; //11.69
        double leftMargin = 0.0; //0.12;
        double rightMargin = 0.0; //0.10;
        double topMargin = 0;
        double bottomMargin = 0.01;
        paper.setSize(paperWidth * 200, paperHeight * 200);

        paper.setImageableArea(
            leftMargin * 200,
            topMargin * 200,
            (paperWidth - leftMargin - rightMargin) * 200,
            (paperHeight - topMargin - bottomMargin) * 200
        );

        format.setPaper(paper);

        aset = new HashPrintRequestAttributeSet();
        aset.add(OrientationRequested.PORTRAIT);
        //testing

    }
}
