package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PosOrderRecord;
import com.mycompany.myapp.domain.Product;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;

public class ReceiptPosPrint implements Printable {

    public PosOrderRecord currentRecord = new PosOrderRecord();

    public ReceiptPosPrint(PosOrderRecord rec) {
        currentRecord = rec;
    }

    SimpleDateFormat df = new SimpleDateFormat();
    String receiptDetailLine;
    public static final String pspace = "               "; // 15-spaces

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        df.applyPattern("dd/MM/yyyy HH:mm:ss");
        String lineDot = "----------------------------------------------------------------------------------";
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int line = 10;

        if (pageIndex < 0 || pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        g2d.drawString(String.format("%-25s", "	-   حكاوي  -"), 1, line);
        line += 13;
        int imagewidth = 160;
        int imageheight = 50;
        BufferedImage read;

        try {
            read = ImageIO.read(getClass().getResource("/image/img1.png"));
            g2d.drawImage(read, 5, line, imagewidth, imageheight, null); // draw image
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        line += 70;
        g2d.drawString(
            String.format(
                "%-25s",
                "ID :  " +
                currentRecord.getId().substring(0, 4) +
                "..." +
                currentRecord.getId().substring(currentRecord.getId().length() - 4)
            ),
            1,
            line
        );

        line += 13;
        g2d.drawString(String.format("%-25s", "DATE : " + currentRecord.getCreatedDate().toString()), 1, line);
        line += 13;
        g2d.drawString(String.format("%-25s", "CASHER : " + currentRecord.getCreatedBy()), 1, line);
        line += 15;
        g2d.drawString(String.format("%-25s", "TABLE :  " + currentRecord.getTable().getName()), 1, line);
        if (currentRecord.getOrdersData() != null && !currentRecord.getOrdersData().isEmpty()) {
            try {
                /* Draw Header */
                int y = line;
                /* Draw Colums */
                g2d.drawLine(10, y + 40, 180, y + 40);

                g2d.drawString("NAME", 2, y + 50);
                g2d.drawString("Q", 110, y + 50);
                g2d.drawString("PRICE", 140, y + 50);
                g2d.drawLine(10, y + 60, 180, y + 60);

                int cH = 0;

                int i = 0;

                for (Product prod : currentRecord.getOrdersData()) {
                    /*
                     * Assume that all parameters are in string data type for this situation All
                     * other premetive data types are accepted.
                     */

                    String itemid = prod.getEnName().length() > 30 ? prod.getEnName().substring(0, 30) + ".." : prod.getEnName();
                    String itemname = currentRecord.getOrdersQuantity().get(prod.getId()).toString();
                    String price = prod.getPrice().toString();

                    cH = (y + 70) + (10 * i); // shifting drawing line

                    g2d.drawString(itemid, 2, cH);
                    g2d.drawString(itemname, 120, cH);
                    g2d.drawString(price + " LE", 140, cH);
                    i++;

                    if (i == currentRecord.getOrdersData().size()) {
                        cH = cH + 10;
                        g2d.drawLine(10, cH, 180, cH);
                        cH = cH + 20;

                        g2d.drawString(
                            String.format("%-25s", "total price :        " + currentRecord.getNetTotalPrice().toString() + " LE"),
                            1,
                            cH
                        );
                        cH += 15;
                        /*	g2d.drawString(lineDot, 1, cH);
						cH += 20;
						g2d.drawString(lineDot, 1, cH);
						cH += 20;
						g2d.drawString(lineDot, 1, cH);
						cH += 20;
						g2d.drawString(lineDot, 1, cH);
						cH += 20;
						g2d.drawString(lineDot, 1, cH); */

                    }
                }
            } catch (Exception r) {
                r.printStackTrace();
            }
        } else {
            g2d.drawString(String.format("%-25s", "total price :        " + currentRecord.getTotalPrice().toString() + " LE"), 1, line);
            line += 15;
        }

        return Printable.PAGE_EXISTS;
    }
}
