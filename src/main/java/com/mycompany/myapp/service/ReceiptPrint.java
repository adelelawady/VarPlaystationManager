package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.table.TableModel;

public class ReceiptPrint implements Printable {

    public Record currentRecord = new Record();

    public ReceiptPrint(Record rec) {
        currentRecord = rec;
    }

    SimpleDateFormat df = new SimpleDateFormat();
    String receiptDetailLine;
    public static final String pspace = "               "; // 15-spaces

    public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2) {
        // Create a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        // Set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
        g2d.setStroke(dashed);

        // Draw to the copy
        g2d.drawLine(x1, y1, x2, y2);

        // Get rid of the copy
        g2d.dispose();
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        String lineDot = "----------------------------------------------------------------------------------";
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Font font = new Font("", Font.BOLD, 10);

        int line = 10;

        if (pageIndex < 0 || pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        g2d.drawString(String.format("%-25s", "	-   VAR   -"), 1, line);
        line += 13;
        int imagewidth = 160;
        int imageheight = 50;
        BufferedImage read;

        try {
            read = ImageIO.read(getClass().getResource("/image/img.jpeg"));
            g2d.drawImage(read, 5, line, imagewidth, imageheight, null); // draw image
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        g2d.setFont(font);
        line += 70;
        //font = new Font("Arial", Font.PLAIN, 9);
        // g2d.setFont(font);

        g2d.drawString(String.format("%-25s", "DEVICE :  " + currentRecord.getDevice().getName()), 1, line);
        line += 13;
        g2d.drawString(
            String.format("%-25s", "TYPE : " + currentRecord.getDevice().getType().getName() + (currentRecord.isMulti() ? "  MULTI " : "")),
            1,
            line
        );

        if (currentRecord.getDuration().toMinutes() > 0) {
            String time = currentRecord.getDuration().toHoursPart() > 0
                ? currentRecord.getDuration().toHoursPart() + " h " + currentRecord.getDuration().toMinutesPart() + " m"
                : currentRecord.getDuration().toMinutesPart() + " m";
            line += 13;
            g2d.drawString(String.format("%-25s", "TIME   : " + time), 1, line);
        }

        if (currentRecord.getTimeDiscount() > 0 || currentRecord.getOrdersDiscount() > 0) {
            line += 13;
            g2d.drawString(
                String.format(
                    "%-25s",
                    "DISCOUNT : " +
                    (currentRecord.getOrdersDiscount() > 0 ? currentRecord.getOrdersDiscount() + " % " : "") +
                    ((currentRecord.getOrdersDiscount() > 0 && currentRecord.getTimeDiscount() > 0) ? " + " : "") +
                    (currentRecord.getTimeDiscount() > 0 ? currentRecord.getTimeDiscount() + " %" : "")
                ),
                1,
                line
            );
        }

        if (!currentRecord.getPreviousSessions().isEmpty()) {
            line += 5;
            g2d.drawLine(10, line + 10, 10, line + 20);
            g2d.drawString("+  " + currentRecord.getDevice().getName() + (currentRecord.isMulti() ? "       MULTI" : ""), 8, line + 10);
            String time = currentRecord.getDuration().toHoursPart() > 0
                ? currentRecord.getDuration().toHoursPart() + " h " + currentRecord.getDuration().toMinutesPart() + " m"
                : currentRecord.getDuration().toMinutesPart() + " m";

            g2d.drawString(time, 15, line + 20);

            g2d.drawString(currentRecord.getTotalPriceTime() + " LE", 120, line + 20);

            line += 20;
            for (Record rec : currentRecord.getPreviousSessions()) {
                g2d.drawLine(10, line + 10, 10, line + 20);
                g2d.drawString("+  " + rec.getDevice().getName() + (rec.isMulti() ? "       MULTI" : ""), 8, line + 10);
                String timex = rec.getDuration().toHoursPart() > 0
                    ? rec.getDuration().toHoursPart() + " h " + rec.getDuration().toMinutesPart() + " m"
                    : rec.getDuration().toMinutesPart() + " m";

                g2d.drawString(timex, 15, line + 20);

                g2d.drawString(rec.getTotalPriceTime() + " LE", 120, line + 20);

                line += 20;
            }
            line += 20;
            if (currentRecord.getPreviousSessionsTotalPrice() > 0) {
                g2d.drawString(
                    String.format("%-25s", "previous devices :        " + currentRecord.getPreviousSessionsTotalPrice().toString() + " LE"),
                    1,
                    line
                );
            }

            line += 20;
        }

        if (currentRecord.getOrdersData() != null && !currentRecord.getOrdersData().isEmpty()) {
            try {
                /* Draw Header */
                int y = line;
                /* Draw Colums */
                g2d.drawLine(10, y + 40, 180, y + 40);

                g2d.drawString("NAME", 10, y + 50);
                g2d.drawString("QTY", 100, y + 50);
                g2d.drawString("PRICE", 140, y + 50);
                g2d.drawLine(10, y + 60, 180, y + 60);

                int cH = 0;

                int i = 0;

                for (Product prod : currentRecord.getOrdersData()) {
                    /*
                     * Assume that all parameters are in string data type for this situation All
                     * other premetive data types are accepted.
                     */
                    String itemid = prod.getEnName().length() > 15 ? prod.getEnName().substring(0, 13) + ".." : prod.getEnName();
                    String itemname = currentRecord.getOrdersQuantity().get(prod.getId()).toString();
                    String price = prod.getPrice().toString();

                    cH = (y + 70) + (10 * i); // shifting drawing line

                    g2d.drawString(toCodedString(itemid), 15, cH);
                    g2d.drawString(itemname, 100, cH);
                    g2d.drawString(price + " LE", 140, cH);
                    i++;

                    if (i == currentRecord.getOrdersData().size()) {
                        cH = cH + 10;
                        g2d.drawLine(10, cH, 180, cH);
                        cH = cH + 20;
                        //font = new Font("Arial", Font.BOLD, 11); // changed font size
                        g2d.setFont(font);

                        if (currentRecord.getDuration().toMinutes() > 0) {
                            g2d.drawString(
                                String.format("%-25s", "Time price :         " + currentRecord.getTotalPriceTime().toString() + " LE"),
                                1,
                                cH
                            );
                            cH += 15;
                        }

                        g2d.drawString(
                            String.format("%-25s", "Orders price :    " + currentRecord.getTotalPriceOrders().toString() + " LE"),
                            1,
                            cH
                        );
                        cH += 15;

                        g2d.drawString(
                            String.format("%-25s", "total price :        " + currentRecord.getTotalPriceUser().toString() + " LE"),
                            1,
                            cH
                        );
                        cH += 15;
                    }
                }
            } catch (Exception r) {
                r.printStackTrace();
            }
        } else {
            line += 15;
            if (currentRecord.getDuration().toMinutes() > 0) {
                g2d.drawString(
                    String.format("%-25s", "Time price :         " + currentRecord.getTotalPriceTime().toString() + " LE"),
                    1,
                    line
                );
                line += 15;
            }

            g2d.drawString(String.format("%-25s", "total price :        " + currentRecord.getTotalPriceUser().toString() + " LE"), 1, line);
            line += 15;
        }

        return Printable.PAGE_EXISTS;
    }

    String toCodedString(String strValue) {
        return new String(strValue.getBytes(), StandardCharsets.UTF_8);
    }
}
