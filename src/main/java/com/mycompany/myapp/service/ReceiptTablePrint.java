package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.TableRecord;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.table.TableModel;

public class ReceiptTablePrint implements Printable {

    public TableRecord currentRecord = new TableRecord();

    public ReceiptTablePrint(TableRecord rec) {
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

        Font font = new Font("Lucida Handwriting", Font.BOLD, 10);
        // Font font = new Font("MS Gothic", Font.PLAIN, 10);

        if (pageIndex < 0 || pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        g2d.drawString(String.format("%-25s", "	-   VAR  -"), 1, line);
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
        font = new Font("MS Gothic", Font.PLAIN, 9);
        g2d.setFont(font);

        g2d.drawString(String.format("%-25s", "TABLE :  " + currentRecord.getTable().getName()), 1, line);

        if (currentRecord.getDiscount() > 0) {
            line += 13;
            g2d.drawString(String.format("%-25s", "DISCOUNT : " + currentRecord.getDiscount() + " %"), 1, line);
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
                    String itemid = prod.getEnName().length() > 15 ? prod.getName().substring(0, 13) + ".." : prod.getName();
                    String itemname = currentRecord.getOrdersQuantity().get(prod.getId()).toString();
                    String price = prod.getPrice().toString();

                    cH = (y + 70) + (10 * i); // shifting drawing line

                    g2d.drawString(itemid, 15, cH);
                    g2d.drawString(itemname, 100, cH);
                    g2d.drawString(price + " LE", 140, cH);
                    i++;

                    if (i == currentRecord.getOrdersData().size()) {
                        cH = cH + 10;
                        g2d.drawLine(10, cH, 180, cH);
                        cH = cH + 20;
                        font = new Font("Arial", Font.BOLD, 11); // changed font size
                        g2d.setFont(font);

                        g2d.drawString(
                            String.format("%-25s", "total price :        " + currentRecord.getTotalPrice().toString() + " LE"),
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
